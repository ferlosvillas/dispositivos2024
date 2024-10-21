```toml
name = 'CrearUsuario'
method = 'POST'
url = '{{baseUrl}}/api/register'
sortWeight = 1000000
id = '48b11edd-341f-41b8-b458-7e64cd3fcd36'

[body]
type = 'JSON'
raw = '''
{
  "login": "{{usuario}}",
  "email": "{{email}}",
  "password": "{{password}}",
  "langKey": "es"
}'''
```
