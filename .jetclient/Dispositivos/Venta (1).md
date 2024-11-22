```toml
name = 'Venta (1)'
description = 'Venta de dispositivo'
method = 'POST'
url = '{{baseUrl}}/api/catedra/vender'
sortWeight = 8000000
id = 'eb760113-5605-4f3f-8793-185cefa1ccec'

[[headers]]
key = 'Content-Type'
value = 'Application/json'

[[headers]]
key = 'Authorization'
value = 'Bearer {{token}}'

[body]
type = 'JSON'
raw = '''
{
  "idDispositivo": 1,
  "personalizaciones": [
    {
      "id": 3,
      "precio": 0.0
    },
    {
      "id": 8,
      "precio": 330.0
    },
    {
      "id": 12,
      "precio": 0.0
    }
  ],
  "adicionales": [
    {
      "id": 1,
      "precio": 0.0
    },
    {
      "id": 2,
      "precio": 78.0
    },
    {
      "id": 4,
      "precio": 0.0
    }
  ],
  "precioFinal": 2847,
  "fechaVenta": "2024-08-10T20:15:00z"
}'''
```
