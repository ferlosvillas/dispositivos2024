```toml
name = 'Login'
method = 'POST'
url = '{{baseUrl}}/api/authenticate'
sortWeight = 3000000
id = '713ccf94-de39-4a1c-95e4-7885c7007daa'

[body]
type = 'JSON'
raw = '''
{
  "username": "{{usuario}}",
  "password": "{{password}}",
  "rememberMe": false
}'''
```
