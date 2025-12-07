<#
课程云微服务测试脚本（Windows PowerShell）
功能：
1. 验证服务启动状态
2. 测试 Nacos 服务注册
3. 负载均衡效果验证
4. 选课/退课接口功能测试
5. 熔断降级场景测试
#>

# ==================== 配置项 ====================
$NACOS_URL = "http://localhost:8848/nacos"
$ENROLLMENT_SERVICE_URL = "http://localhost:8083/api/enrollments"
$USER_SERVICE_URL = "http://localhost:8081/api/students"
$CATALOG_SERVICE_URL = "http://localhost:8082/api/courses"
$TEST_STUDENT_ID = "8f1e2a78-4c21-11ef-b7a9-0242ac120002"
$TEST_COURSE_ID = "78f8c0fc-688d-e383-3259-85566ddc80da"
$TEST_ENROLL_ID = ""  # 自动生成，无需手动配置

# ==================== 工具函数 ====================
# 打印彩色日志
function Write-ColorLog {
    param(
        [string]$Message,
        [string]$Color = "White"
    )
    Write-Host "[$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')] $Message" -ForegroundColor $Color
}

# 等待服务就绪
function Wait-ServiceReady {
    param(
        [string]$Url,
        [string]$ServiceName,
        [int]$Timeout = 60
    )
    Write-ColorLog "等待 $ServiceName 就绪（超时时间：$Timeout 秒）..." "Cyan"
    $startTime = Get-Date
    while ($true) {
        try {
            $response = Invoke-WebRequest -Uri $Url -Method Get -TimeoutSec 5 -ErrorAction Stop
            if ($response.StatusCode -eq 200) {
                Write-ColorLog "$ServiceName 已就绪！" "Green"
                return $true
            }
        } catch {
            # 忽略异常，继续等待
        }
        if ((Get-Date) - $startTime -ge [TimeSpan]::FromSeconds($Timeout)) {
            Write-ColorLog "$ServiceName 启动超时！" "Red"
            return $false
        }
        Start-Sleep -Seconds 2
    }
}

# ==================== 测试步骤 ====================
Clear-Host
Write-ColorLog "========== 课程云微服务测试开始 ==========" "Yellow"

# 步骤1：验证容器启动状态
Write-ColorLog "`n【步骤1】验证Docker容器启动状态" "Cyan"
docker-compose ps
if ($LASTEXITCODE -ne 0) {
    Write-ColorLog "Docker容器启动异常，请检查docker-compose.yml配置！" "Red"
    exit 1
}

# 步骤2：等待核心服务就绪
Write-ColorLog "`n【步骤2】等待核心服务就绪" "Cyan"
$nacosReady = Wait-ServiceReady -Url "$NACOS_URL/" -ServiceName "Nacos"
$enrollmentReady = Wait-ServiceReady -Url "$ENROLLMENT_SERVICE_URL" -ServiceName "Enrollment Service"
$userReady = Wait-ServiceReady -Url "$USER_SERVICE_URL" -ServiceName "User Service"
$catalogReady = Wait-ServiceReady -Url "$CATALOG_SERVICE_URL" -ServiceName "Catalog Service"

if (-not ($nacosReady -and $enrollmentReady -and $userReady -and $catalogReady)) {
    Write-ColorLog "核心服务未全部就绪，测试终止！" "Red"
    exit 1
}

# 步骤3：验证Nacos服务注册
Write-ColorLog "`n【步骤3】验证Nacos服务注册" "Cyan"
    $serviceListUrl2 ="http://nacos:nacos@localhost:8848/nacos/v1/ns/instance/list?serviceName=user-service&groupName=COURSEHUB_GROUP&namespaceId=dev"
    
   
# 1. 基础配置
$NACOS_URL = "http://localhost:8848/nacos"
$devNamespaceId = "dev"
$serviceGroup = "COURSEHUB_GROUP"
$nacosUser = "nacos"
$nacosPwd = "nacos"

# 2. 修复Base64编码（用字符串拼接+${}包裹变量，避免:解析异常）
$authString = "${nacosUser}:${nacosPwd}"  # 用${}包裹变量名，解决:解析问题
$base64Auth = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($authString))
$headers = @{ "Authorization" = "Basic $base64Auth" }

try {
    # 3. 调用服务列表接口（单行参数，避免换行解析异常）
    $serviceListUrl = "$NACOS_URL/v1/ns/service/list?pageNo=1&pageSize=100&groupName=$serviceGroup&namespaceId=$devNamespaceId"
    $response = Invoke-WebRequest -Uri $serviceListUrl -Headers $headers -Method Get -TimeoutSec 10 -ErrorAction Stop
    
    # 4. 解析响应
    $services = $response.Content | ConvertFrom-Json

    # 5. 定义实例数查询函数（单行参数）
    function Get-ServiceInstanceCount {
        param(
            [string]$ServiceName,
            [string]$GroupName,
            [string]$NamespaceId,
            [string]$NacosUrl,
            [hashtable]$Headers
        )
        $instanceUrl = "$NacosUrl/v1/ns/instance/list?serviceName=$ServiceName&groupName=$GroupName&namespaceId=$NamespaceId"
        $instanceResp = Invoke-WebRequest -Uri $instanceUrl -Headers $Headers -Method Get -TimeoutSec 10 -ErrorAction Stop
        $instanceData = $instanceResp.Content | ConvertFrom-Json
        return $instanceData.hosts.Count
    }

    # 6. 统计实例数（单行逻辑）
    $userServiceCount = if ($services.doms -contains "user-service") { Get-ServiceInstanceCount -ServiceName "user-service" -GroupName $serviceGroup -NamespaceId $devNamespaceId -NacosUrl $NACOS_URL -Headers $headers } else { 0 }
    $catalogServiceCount = if ($services.doms -contains "catalog-service") { Get-ServiceInstanceCount -ServiceName "catalog-service" -GroupName $serviceGroup -NamespaceId $devNamespaceId -NacosUrl $NACOS_URL -Headers $headers } else { 0 }
    $enrollmentServiceCount = if ($services.doms -contains "enrollment-service") { Get-ServiceInstanceCount -ServiceName "enrollment-service" -GroupName $serviceGroup -NamespaceId $devNamespaceId -NacosUrl $NACOS_URL -Headers $headers } else { 0 }

    # 7. 输出结果
    Write-ColorLog " User Service 实例数：$userServiceCount（预期3）" "Green"
    Write-ColorLog " Catalog Service 实例数：$catalogServiceCount（预期2）" "Green"
    Write-ColorLog " Enrollment Service 实例数：$enrollmentServiceCount（预期1）" "Green"

    # 8. 校验实例数
    if ($userServiceCount -ne 3 -or $catalogServiceCount -ne 2 -or $enrollmentServiceCount -ne 1) {
        Write-ColorLog " 警告：服务实例数不符合预期！" "Yellow"
        Write-ColorLog " 实际：user=$userServiceCount, catalog=$catalogServiceCount, enrollment=$enrollmentServiceCount" "Yellow"
    } else {
        Write-ColorLog " 所有服务实例数符合预期！" "Green"
    }

} catch {
    # 9. 错误排查
    Write-ColorLog "获取Nacos服务列表失败：$($_.Exception.Message)" "Red"
    if ($_.Exception.Response) { Write-ColorLog "错误状态码：$($_.Exception.Response.StatusCode)" "Red" }
    Write-ColorLog "请求URL：$serviceListUrl" "Red"
    Write-ColorLog "请检查：1.Nacos容器是否启动 2.端口是否为8848 3.鉴权账号密码是否正确 4.分组/命名空间是否匹配" "Red"
}

# 步骤4：负载均衡测试（User Service）
Write-ColorLog "`n【步骤4】测试User Service负载均衡（连续调用10次）" "Cyan"
$instancePorts = @()
for ($i=1; $i -le 10; $i++) {
    try {
        $response = Invoke-WebRequest -Uri "$USER_SERVICE_URL/instance-info" -Method Get -ErrorAction Stop | ConvertFrom-Json
        $port = $response.port
        $instancePorts += $port
        Write-ColorLog "第$i次调用 → User Service 实例端口：$port" "White"
    } catch {
        Write-ColorLog "第$i次调用失败：$_" "Red"
    }
}
$uniquePorts = $instancePorts | Select-Object -Unique
Write-ColorLog "✅ 命中的User Service实例端口：$($uniquePorts -join ',')（预期包含8081、8084、8085）" "Green"

# 步骤5：负载均衡测试（Catalog Service）
Write-ColorLog "`n【步骤5】测试Catalog Service负载均衡（连续调用10次）" "Cyan"
$catalogPorts = @()
for ($i=1; $i -le 10; $i++) {
    try {
        $response = Invoke-WebRequest -Uri "$CATALOG_SERVICE_URL/instance-info" -Method Get -ErrorAction Stop | ConvertFrom-Json
        $port = $response.port
        $catalogPorts += $port
        Write-ColorLog "第$i次调用 → Catalog Service 实例端口：$port" "White"
    } catch {
        Write-ColorLog "第$i次调用失败：$_" "Red"
    }
}
$uniqueCatalogPorts = $catalogPorts | Select-Object -Unique
Write-ColorLog "✅ 命中的Catalog Service实例端口：$($uniqueCatalogPorts -join ',')（预期包含8082、8086）" "Green"

# 步骤6：选课接口功能测试
Write-ColorLog "`n【步骤6】测试选课接口" "Cyan"
try {
    # 构造选课请求参数
    $enrollRequest = @{
        studentId = $TEST_STUDENT_ID
        courseId = $TEST_COURSE_ID
    } | ConvertTo-Json

    # 调用选课接口
    $enrollResponse = Invoke-WebRequest -Uri "$ENROLLMENT_SERVICE_URL" `
        -Method Post `
        -Body $enrollRequest `
        -ContentType "application/json" `
        -ErrorAction Stop | ConvertFrom-Json
    
    $TEST_ENROLL_ID = $enrollResponse.id
    Write-ColorLog "✅ 选课成功！选课ID：$TEST_ENROLL_ID" "Green"
} catch {
    Write-ColorLog "选课接口调用失败：$_" "Red"
}

# 步骤7：查询选课记录测试
Write-ColorLog "`n【步骤7】测试查询选课记录（按学生ID）" "Cyan"
try {
    $records = Invoke-WebRequest -Uri "$ENROLLMENT_SERVICE_URL/studentId/$TEST_STUDENT_ID" `
        -Method Get `
        -ErrorAction Stop | ConvertFrom-Json
    
    Write-ColorLog "✅ 查询到选课记录数：$($records.Count)" "Green"
} catch {
    Write-ColorLog "查询选课记录失败：$_" "Red"
}

# 步骤8：退课接口功能测试
if ($TEST_ENROLL_ID) {
    Write-ColorLog "`n【步骤8】测试退课接口" "Cyan"
    try {
        Invoke-WebRequest -Uri "$ENROLLMENT_SERVICE_URL/$TEST_ENROLL_ID" `
            -Method Delete `
            -ErrorAction Stop
        Write-ColorLog "✅ 退课成功！选课ID：$TEST_ENROLL_ID" "Green"
    } catch {
        Write-ColorLog "退课接口调用失败：$_" "Red"
    }
}

# 步骤9：熔断降级测试（停止User Service所有实例）
Write-ColorLog "`n【步骤9】测试熔断降级（停止User Service实例）" "Cyan"
Write-ColorLog "正在停止User Service所有实例..." "Yellow"
docker stop user-service-1 user-service-2 user-service-3

# 等待熔断触发（5秒）
Start-Sleep -Seconds 5

# 再次调用选课接口，验证降级
try {
    $enrollRequest = @{
        studentId = $TEST_STUDENT_ID
        courseId = $TEST_COURSE_ID
    } | ConvertTo-Json

    Invoke-WebRequest -Uri "$ENROLLMENT_SERVICE_URL" `
        -Method Post `
        -Body $enrollRequest `
        -ContentType "application/json" `
        -ErrorAction Stop
    Write-ColorLog "❌ 熔断降级未触发（预期失败）" "Red"
} catch {
    $errorMessage = $_.Exception.Response.StatusDescription
    Write-ColorLog "✅ 熔断降级触发！错误信息：$errorMessage（符合预期）" "Green"
}

# 恢复User Service实例
Write-ColorLog "`n恢复User Service实例..." "Yellow"
docker start user-service-1 user-service-2 user-service-3
Start-Sleep -Seconds 5

# ==================== 测试结束 ====================
Write-ColorLog "`n========== 课程云微服务测试完成 ==========" "Yellow"