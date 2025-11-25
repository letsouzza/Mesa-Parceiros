$filePath = "C:\Users\subli\StudioProjects\Mesa-Parceiros\app\src\main\java\br\senai\sp\jandira\mesaparceiros\service\EmpresaService.kt"
$lines = Get-Content $filePath

# Remove a última linha (})
$lines = $lines[0..($lines.Count-2)]

# Adiciona o novo endpoint
$lines += ""
$lines += "    @Headers(`"Content-Type: application/json`")"
$lines += "    @PUT(`"empresa/{id}`")"
$lines += "    fun updateEmpresa(@Path(`"id`") id: Int, @Body empresa: EmpresaUpdate): Call<ResponseGeral>"
$lines += "}"

# Salva o arquivo
Set-Content $filePath -Value $lines
