```toml
name = 'Venta'
description = 'Venta de dispositivo'
method = 'POST'
url = '{{baseUrl}}/api/catedra/vender'
sortWeight = 3000000
id = 'f15181cc-948f-4e9f-861f-556140633c13'

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
  "idDispositivo": 2,
  "personalizaciones": [
    {
      "id": 3,
      "precio": 0.0
    }, {
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
    } ],
  "precioFinal": 2800,
  "fechaVenta": "2024-10-07T07:25:00z"
}'''
```
