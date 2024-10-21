```toml
name = 'GetVentas'
method = 'GET'
url = '{{baseUrl}}/api/catedra/ventas'
sortWeight = 4000000
id = '1930a707-cdd7-4f98-a893-5996fb0c25db'

[[headers]]
key = 'Content-Type'
value = 'application/json'

[[headers]]
key = 'Authorization'
value = 'Bearer {{token}}'
```
