```toml
name = 'GetDispositivos'
method = 'GET'
url = '{{baseUrl}}/api/catedra/dispositivos'
sortWeight = 1000000
id = 'a7fde8a5-f577-4b04-8b15-d44a50e6e7ba'

[[headers]]
key = 'Content-Type'
value = 'application/json'

[[headers]]
key = 'Authorization'
value = 'Bearer {{token}}'

[auth]
type = 'NO_AUTH'
```
