# metrics

This is an example metrics app.  
It consists of a backend and a frontend

## Architecture
The app flow is as follows:
- An ingest rest endpoint pushes the raw data to a kafka topic.


- A kakfa streams app which aggregates the metrics group by second with a sliding windows of 15m with a grace period of 1 hour:  
That means that events are agrregated per second and can send events "late" up to one hour.


- A query endpoint queries the timescaledb for fetching the points for the last 15m, last hour or last day.


- A very simple react app where fetch metrics and post metrics


NOTE: the insertion of metrics is not instantaneous, they take a while to be processed.


NOTE: timestaps are always in epcho time in milliseconds in UTC zone.  
Refer to [https://www.epochconverter.com/](https://www.epochconverter.com/) if you need to generate timestamps, make sure to use GMT.

## Endpoint definitions

### Ingest api
```bash
curl --location --request POST 'localhost:8080/ingest' \
--header 'Content-Type: application/json' \
--data-raw '{"metrics":[
    {"metric":"cpu1","points":[{"timestamp":"1646221951000","value":"1.9"},{"timestamp":"1646221961000","value":"1.1"},{"timestamp":"1646222071000","value":"1.1"}  ]},
    {"metric":"men1","points":[{"timestamp":"1646221951000","value":"2.9"},{"timestamp":"1646221961000","value":"1.1"} ,{"timestamp":"1646222071000","value":"1.1"} ]},
    {"metric":"men2","points":[{"timestamp":"1646221951000","value":"5.9"},{"timestamp":"1646221961000","value":"1.1"},{"timestamp":"1646222071000","value":"1.1"}  ]},
    {"metric":"cpu2","points":[{"timestamp":"1646221951000","value":"9.9"},{"timestamp":"1646221961000","value":"1.1"} ,{"timestamp":"1646222071000","value":"1.1"} ]}
]}'
```

Response:
```json
{
    "result": "ok"
}
```

### Query api
```bash
curl --location --request GET 'http://127.0.0.1:8080/timeseries?metric=cpu2&timeRange=LAST_15M&timestamp=1646222071000'
```
timeRange can be: LAST_15M, LAST_HOUR, LAST_DAY:  
- LAST_15M will return the raw points per second. So, expect up to 900 points  
- LAST_HOUR will return the points aggregated per minute: so, expect up to 60 points
- LAST_DAY will return the points aggregated per 30 minutes: so, expect up to 48 points

Response
````json
{
    "metric": "cpu2",
    "timestamp": [
        {
            "timestamp": 1646221951000,
            "value": 7.0
        },
        {
            "timestamp": 1646221961000,
            "value": 1.1
        },
        {
            "timestamp": 1646222071000,
            "value": 1.1
        }
    ]
}
````

## Build and run
To build, execute
```bash
docker-compose build
```

To run, execute
```bash
docker-compose up
```

And open your browser at [localhost:3000](localhost:3000)

alternatively use the example curls in the Endpoints sections to play with it.


## What I would like to add?

#### Testing:
All the classes are really interacting with infra, so I would like to add test for each one of them with mocked dependencies, unfortunately I can not devote more time to this test. so there's no test.
But, please take a look at these at another examples for see own I usually test:

This is a mergesort algorithm implementation for sorting extra large files: in the GB
[https://github.com/tgracchus-zz/mergersort](https://github.com/tgracchus-zz/mergersort)

#### Better frontend:
I just created a very simple frontend. Ideally I would like to improve it.
