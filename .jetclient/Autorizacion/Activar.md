```toml
name = 'Activar'
method = 'POST'
url = '{{baseUrl}}/api/activar'
sortWeight = 2000000
id = '3eec0a60-83d5-474d-ba0f-d171a631c279'

[body]
type = 'JSON'
raw = '''
{
  "login": "{{usuario}}",
  "email": "{{email}}",
  "nombres": "Fernando Villarreal",
  "descripcion": "El profe"
}'''
```
