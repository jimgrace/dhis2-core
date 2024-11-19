var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name--1146707516",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "103",
        "ok": "103",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "371",
        "ok": "371",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1694",
        "ok": "1694",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "1040",
        "ok": "1040",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "232",
        "ok": "232",
        "ko": "-"
    },
    "percentiles1": {
        "total": "1024",
        "ok": "1024",
        "ko": "-"
    },
    "percentiles2": {
        "total": "1200",
        "ok": "1200",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1415",
        "ok": "1415",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1536",
        "ok": "1536",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 16,
    "percentage": 15.53398058252427
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 61,
    "percentage": 59.22330097087378
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 26,
    "percentage": 25.24271844660194
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "6.44",
        "ok": "6.44",
        "ko": "-"
    }
},
contents: {
"req_-api-analytics--690187672": {
        type: "REQUEST",
        name: "/api/analytics/enrollments/query/SSLpOM0r1U7?dimension=ou:IWp9dQGM0bS;W6sNfkJcXGC;LEVEL-b5jE033nBqM;LEVEL-vFr4zVw6Avn;OU_GROUP-YXlxwXEWex6;OU_GROUP-roGQQw4l3dW;OU_GROUP-VePuVPFoyJ2,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX:IN:1;NV;0,s53RFfXA75f[0].LauCl9aicLX:IN:1;NV;0,HI9Y7BKVNnC,JQC3DLdCWK8:GE:0,dkaVGV1WUCR,lXDpHyE8wgb&headers=ouname,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX,s53RFfXA75f[0].LauCl9aicLX,HI9Y7BKVNnC,JQC3DLdCWK8,dkaVGV1WUCR,lXDpHyE8wgb&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
path: "/api/analytics/enrollments/query/SSLpOM0r1U7?dimension=ou:IWp9dQGM0bS;W6sNfkJcXGC;LEVEL-b5jE033nBqM;LEVEL-vFr4zVw6Avn;OU_GROUP-YXlxwXEWex6;OU_GROUP-roGQQw4l3dW;OU_GROUP-VePuVPFoyJ2,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX:IN:1;NV;0,s53RFfXA75f[0].LauCl9aicLX:IN:1;NV;0,HI9Y7BKVNnC,JQC3DLdCWK8:GE:0,dkaVGV1WUCR,lXDpHyE8wgb&headers=ouname,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX,s53RFfXA75f[0].LauCl9aicLX,HI9Y7BKVNnC,JQC3DLdCWK8,dkaVGV1WUCR,lXDpHyE8wgb&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
pathFormatted: "req_-api-analytics--690187672",
stats: {
    "name": "/api/analytics/enrollments/query/SSLpOM0r1U7?dimension=ou:IWp9dQGM0bS;W6sNfkJcXGC;LEVEL-b5jE033nBqM;LEVEL-vFr4zVw6Avn;OU_GROUP-YXlxwXEWex6;OU_GROUP-roGQQw4l3dW;OU_GROUP-VePuVPFoyJ2,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX:IN:1;NV;0,s53RFfXA75f[0].LauCl9aicLX:IN:1;NV;0,HI9Y7BKVNnC,JQC3DLdCWK8:GE:0,dkaVGV1WUCR,lXDpHyE8wgb&headers=ouname,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX,s53RFfXA75f[0].LauCl9aicLX,HI9Y7BKVNnC,JQC3DLdCWK8,dkaVGV1WUCR,lXDpHyE8wgb&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
    "numberOfRequests": {
        "total": "13",
        "ok": "13",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "949",
        "ok": "949",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1350",
        "ok": "1350",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "1199",
        "ok": "1199",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "112",
        "ok": "112",
        "ko": "-"
    },
    "percentiles1": {
        "total": "1202",
        "ok": "1202",
        "ko": "-"
    },
    "percentiles2": {
        "total": "1284",
        "ok": "1284",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1338",
        "ok": "1338",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1348",
        "ok": "1348",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0.0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 6,
    "percentage": 46.15384615384615
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 7,
    "percentage": 53.84615384615385
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.81",
        "ok": "0.81",
        "ko": "-"
    }
}
    },"req_-api-analytics--1703293855": {
        type: "REQUEST",
        name: "/api/analytics/enrollments/query/M3xtLkYBlKI?dimension=ou:IWp9dQGM0bS;W6sNfkJcXGC;LEVEL-b5jE033nBqM;LEVEL-vFr4zVw6Avn;OU_GROUP-YXlxwXEWex6;OU_GROUP-roGQQw4l3dW;OU_GROUP-VePuVPFoyJ2,cl2RC5MLQYO:GE:0,gDgZ5oXCyWm,DishKl0ppXK,nO7nsEjYcp5,zyTL3AMIkf2,OHeZKzifNYE,d6Sr0B2NJYv,yZmG3RbbBKG,uvMKOn1oWvd.yhX7ljWZV9q:IN:NV,uvMKOn1oWvd.JhpYDsTUfi2:IN:1,CWaAcQYKVpq[1].dbMsAGvictz,CWaAcQYKVpq[2].dbMsAGvictz,CWaAcQYKVpq[0].dbMsAGvictz,CWaAcQYKVpq.ehBd9cR5bq4:EQ:NV,CWaAcQYKVpq.VNM6zoPECqd:GT:0,CWaAcQYKVpq.SaHE38QFFwZ:IN:HILLY_AND_PLATUE;PLATUE;HILLY&headers=ouname,cl2RC5MLQYO,gDgZ5oXCyWm,DishKl0ppXK,nO7nsEjYcp5,zyTL3AMIkf2,OHeZKzifNYE,d6Sr0B2NJYv,yZmG3RbbBKG,uvMKOn1oWvd.yhX7ljWZV9q,uvMKOn1oWvd.JhpYDsTUfi2,CWaAcQYKVpq[1].dbMsAGvictz,CWaAcQYKVpq[2].dbMsAGvictz,CWaAcQYKVpq[0].dbMsAGvictz,CWaAcQYKVpq.ehBd9cR5bq4,CWaAcQYKVpq.VNM6zoPECqd,CWaAcQYKVpq.SaHE38QFFwZ,createdbydisplayname&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&programStatus=COMPLETED,ACTIVE&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
path: "/api/analytics/enrollments/query/M3xtLkYBlKI?dimension=ou:IWp9dQGM0bS;W6sNfkJcXGC;LEVEL-b5jE033nBqM;LEVEL-vFr4zVw6Avn;OU_GROUP-YXlxwXEWex6;OU_GROUP-roGQQw4l3dW;OU_GROUP-VePuVPFoyJ2,cl2RC5MLQYO:GE:0,gDgZ5oXCyWm,DishKl0ppXK,nO7nsEjYcp5,zyTL3AMIkf2,OHeZKzifNYE,d6Sr0B2NJYv,yZmG3RbbBKG,uvMKOn1oWvd.yhX7ljWZV9q:IN:NV,uvMKOn1oWvd.JhpYDsTUfi2:IN:1,CWaAcQYKVpq[1].dbMsAGvictz,CWaAcQYKVpq[2].dbMsAGvictz,CWaAcQYKVpq[0].dbMsAGvictz,CWaAcQYKVpq.ehBd9cR5bq4:EQ:NV,CWaAcQYKVpq.VNM6zoPECqd:GT:0,CWaAcQYKVpq.SaHE38QFFwZ:IN:HILLY_AND_PLATUE;PLATUE;HILLY&headers=ouname,cl2RC5MLQYO,gDgZ5oXCyWm,DishKl0ppXK,nO7nsEjYcp5,zyTL3AMIkf2,OHeZKzifNYE,d6Sr0B2NJYv,yZmG3RbbBKG,uvMKOn1oWvd.yhX7ljWZV9q,uvMKOn1oWvd.JhpYDsTUfi2,CWaAcQYKVpq[1].dbMsAGvictz,CWaAcQYKVpq[2].dbMsAGvictz,CWaAcQYKVpq[0].dbMsAGvictz,CWaAcQYKVpq.ehBd9cR5bq4,CWaAcQYKVpq.VNM6zoPECqd,CWaAcQYKVpq.SaHE38QFFwZ,createdbydisplayname&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&programStatus=COMPLETED,ACTIVE&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
pathFormatted: "req_-api-analytics--1703293855",
stats: {
    "name": "/api/analytics/enrollments/query/M3xtLkYBlKI?dimension=ou:IWp9dQGM0bS;W6sNfkJcXGC;LEVEL-b5jE033nBqM;LEVEL-vFr4zVw6Avn;OU_GROUP-YXlxwXEWex6;OU_GROUP-roGQQw4l3dW;OU_GROUP-VePuVPFoyJ2,cl2RC5MLQYO:GE:0,gDgZ5oXCyWm,DishKl0ppXK,nO7nsEjYcp5,zyTL3AMIkf2,OHeZKzifNYE,d6Sr0B2NJYv,yZmG3RbbBKG,uvMKOn1oWvd.yhX7ljWZV9q:IN:NV,uvMKOn1oWvd.JhpYDsTUfi2:IN:1,CWaAcQYKVpq[1].dbMsAGvictz,CWaAcQYKVpq[2].dbMsAGvictz,CWaAcQYKVpq[0].dbMsAGvictz,CWaAcQYKVpq.ehBd9cR5bq4:EQ:NV,CWaAcQYKVpq.VNM6zoPECqd:GT:0,CWaAcQYKVpq.SaHE38QFFwZ:IN:HILLY_AND_PLATUE;PLATUE;HILLY&headers=ouname,cl2RC5MLQYO,gDgZ5oXCyWm,DishKl0ppXK,nO7nsEjYcp5,zyTL3AMIkf2,OHeZKzifNYE,d6Sr0B2NJYv,yZmG3RbbBKG,uvMKOn1oWvd.yhX7ljWZV9q,uvMKOn1oWvd.JhpYDsTUfi2,CWaAcQYKVpq[1].dbMsAGvictz,CWaAcQYKVpq[2].dbMsAGvictz,CWaAcQYKVpq[0].dbMsAGvictz,CWaAcQYKVpq.ehBd9cR5bq4,CWaAcQYKVpq.VNM6zoPECqd,CWaAcQYKVpq.SaHE38QFFwZ,createdbydisplayname&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&programStatus=COMPLETED,ACTIVE&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
    "numberOfRequests": {
        "total": "13",
        "ok": "13",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "915",
        "ok": "915",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1427",
        "ok": "1427",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "1163",
        "ok": "1163",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "129",
        "ok": "129",
        "ko": "-"
    },
    "percentiles1": {
        "total": "1165",
        "ok": "1165",
        "ko": "-"
    },
    "percentiles2": {
        "total": "1230",
        "ok": "1230",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1347",
        "ok": "1347",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1411",
        "ok": "1411",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0.0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 8,
    "percentage": 61.53846153846154
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 5,
    "percentage": 38.46153846153847
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.81",
        "ok": "0.81",
        "ko": "-"
    }
}
    },"req_-api-analytics---46546211": {
        type: "REQUEST",
        name: "/api/analytics/enrollments/query/pMIglSEqPGS?dimension=ou:IWp9dQGM0bS;W6sNfkJcXGC;LEVEL-b5jE033nBqM;LEVEL-vFr4zVw6Avn;OU_GROUP-YXlxwXEWex6;OU_GROUP-roGQQw4l3dW;OU_GROUP-VePuVPFoyJ2,JxX12764mmB,ENRjVGxVL6l:ILIKE:a,sB1IHYu2xQT:!ILIKE:r,D917mo9Whvn:IN:1;NV&filter=qDVxzO0Ilkq:GT:0&headers=ouname,JxX12764mmB,ENRjVGxVL6l,sB1IHYu2xQT,D917mo9Whvn&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
path: "/api/analytics/enrollments/query/pMIglSEqPGS?dimension=ou:IWp9dQGM0bS;W6sNfkJcXGC;LEVEL-b5jE033nBqM;LEVEL-vFr4zVw6Avn;OU_GROUP-YXlxwXEWex6;OU_GROUP-roGQQw4l3dW;OU_GROUP-VePuVPFoyJ2,JxX12764mmB,ENRjVGxVL6l:ILIKE:a,sB1IHYu2xQT:!ILIKE:r,D917mo9Whvn:IN:1;NV&filter=qDVxzO0Ilkq:GT:0&headers=ouname,JxX12764mmB,ENRjVGxVL6l,sB1IHYu2xQT,D917mo9Whvn&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
pathFormatted: "req_-api-analytics---46546211",
stats: {
    "name": "/api/analytics/enrollments/query/pMIglSEqPGS?dimension=ou:IWp9dQGM0bS;W6sNfkJcXGC;LEVEL-b5jE033nBqM;LEVEL-vFr4zVw6Avn;OU_GROUP-YXlxwXEWex6;OU_GROUP-roGQQw4l3dW;OU_GROUP-VePuVPFoyJ2,JxX12764mmB,ENRjVGxVL6l:ILIKE:a,sB1IHYu2xQT:!ILIKE:r,D917mo9Whvn:IN:1;NV&filter=qDVxzO0Ilkq:GT:0&headers=ouname,JxX12764mmB,ENRjVGxVL6l,sB1IHYu2xQT,D917mo9Whvn&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
    "numberOfRequests": {
        "total": "17",
        "ok": "17",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "582",
        "ok": "582",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1154",
        "ok": "1154",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "905",
        "ok": "905",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "126",
        "ok": "126",
        "ko": "-"
    },
    "percentiles1": {
        "total": "905",
        "ok": "905",
        "ko": "-"
    },
    "percentiles2": {
        "total": "950",
        "ok": "950",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1109",
        "ok": "1109",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1145",
        "ok": "1145",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 2,
    "percentage": 11.76470588235294
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 15,
    "percentage": 88.23529411764706
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "1.06",
        "ok": "1.06",
        "ko": "-"
    }
}
    },"req_-api-analytics--351119387": {
        type: "REQUEST",
        name: "/api/analytics/enrollments/query/Lt6P15ps7f6?dimension=ou:USER_ORGUNIT,ntelZthDPpR,WlgSirxU9bG,oindugucx72,OrvVU0I9wTT:EQ:1&headers=enrollmentdate,ouname,ntelZthDPpR,WlgSirxU9bG,oindugucx72,OrvVU0I9wTT&totalPages=false&enrollmentDate=THIS_YEAR,LAST_5_YEARS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
path: "/api/analytics/enrollments/query/Lt6P15ps7f6?dimension=ou:USER_ORGUNIT,ntelZthDPpR,WlgSirxU9bG,oindugucx72,OrvVU0I9wTT:EQ:1&headers=enrollmentdate,ouname,ntelZthDPpR,WlgSirxU9bG,oindugucx72,OrvVU0I9wTT&totalPages=false&enrollmentDate=THIS_YEAR,LAST_5_YEARS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
pathFormatted: "req_-api-analytics--351119387",
stats: {
    "name": "/api/analytics/enrollments/query/Lt6P15ps7f6?dimension=ou:USER_ORGUNIT,ntelZthDPpR,WlgSirxU9bG,oindugucx72,OrvVU0I9wTT:EQ:1&headers=enrollmentdate,ouname,ntelZthDPpR,WlgSirxU9bG,oindugucx72,OrvVU0I9wTT&totalPages=false&enrollmentDate=THIS_YEAR,LAST_5_YEARS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
    "numberOfRequests": {
        "total": "17",
        "ok": "17",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "673",
        "ok": "673",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1231",
        "ok": "1231",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "896",
        "ok": "896",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "160",
        "ok": "160",
        "ko": "-"
    },
    "percentiles1": {
        "total": "912",
        "ok": "912",
        "ko": "-"
    },
    "percentiles2": {
        "total": "995",
        "ok": "995",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1149",
        "ok": "1149",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1215",
        "ok": "1215",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 7,
    "percentage": 41.17647058823529
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 9,
    "percentage": 52.94117647058824
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 1,
    "percentage": 5.88235294117647
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "1.06",
        "ok": "1.06",
        "ko": "-"
    }
}
    },"req_-api-analytics---741163252": {
        type: "REQUEST",
        name: "/api/analytics/enrollments/query/KYzHf1Ta6C4?dimension=ou:USER_ORGUNIT,BdvE9shT6GX,bE3YcdNxA3g:GT:1&headers=enrollmentdate,ouname,BdvE9shT6GX,bE3YcdNxA3g&totalPages=false&enrollmentDate=THIS_YEAR,LAST_YEAR&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
path: "/api/analytics/enrollments/query/KYzHf1Ta6C4?dimension=ou:USER_ORGUNIT,BdvE9shT6GX,bE3YcdNxA3g:GT:1&headers=enrollmentdate,ouname,BdvE9shT6GX,bE3YcdNxA3g&totalPages=false&enrollmentDate=THIS_YEAR,LAST_YEAR&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
pathFormatted: "req_-api-analytics---741163252",
stats: {
    "name": "/api/analytics/enrollments/query/KYzHf1Ta6C4?dimension=ou:USER_ORGUNIT,BdvE9shT6GX,bE3YcdNxA3g:GT:1&headers=enrollmentdate,ouname,BdvE9shT6GX,bE3YcdNxA3g&totalPages=false&enrollmentDate=THIS_YEAR,LAST_YEAR&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
    "numberOfRequests": {
        "total": "14",
        "ok": "14",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "799",
        "ok": "799",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1368",
        "ok": "1368",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "1079",
        "ok": "1079",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "155",
        "ok": "155",
        "ko": "-"
    },
    "percentiles1": {
        "total": "1083",
        "ok": "1083",
        "ko": "-"
    },
    "percentiles2": {
        "total": "1167",
        "ok": "1167",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1341",
        "ok": "1341",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1363",
        "ok": "1363",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 1,
    "percentage": 7.142857142857142
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 10,
    "percentage": 71.42857142857143
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 3,
    "percentage": 21.428571428571427
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.88",
        "ok": "0.88",
        "ko": "-"
    }
}
    },"req_-api-analytics--238133834": {
        type: "REQUEST",
        name: "/api/analytics/enrollments/query/SSLpOM0r1U7?dimension=ou:USER_ORGUNIT;USER_ORGUNIT_CHILDREN;USER_ORGUNIT_GRANDCHILDREN,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX:IN:1;NV;0,s53RFfXA75f[0].LauCl9aicLX:IN:1;NV;0,HI9Y7BKVNnC,JQC3DLdCWK8:GE:0,dkaVGV1WUCR,lXDpHyE8wgb&headers=ouname,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX,s53RFfXA75f[0].LauCl9aicLX,HI9Y7BKVNnC,JQC3DLdCWK8,dkaVGV1WUCR,lXDpHyE8wgb&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
path: "/api/analytics/enrollments/query/SSLpOM0r1U7?dimension=ou:USER_ORGUNIT;USER_ORGUNIT_CHILDREN;USER_ORGUNIT_GRANDCHILDREN,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX:IN:1;NV;0,s53RFfXA75f[0].LauCl9aicLX:IN:1;NV;0,HI9Y7BKVNnC,JQC3DLdCWK8:GE:0,dkaVGV1WUCR,lXDpHyE8wgb&headers=ouname,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX,s53RFfXA75f[0].LauCl9aicLX,HI9Y7BKVNnC,JQC3DLdCWK8,dkaVGV1WUCR,lXDpHyE8wgb&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
pathFormatted: "req_-api-analytics--238133834",
stats: {
    "name": "/api/analytics/enrollments/query/SSLpOM0r1U7?dimension=ou:USER_ORGUNIT;USER_ORGUNIT_CHILDREN;USER_ORGUNIT_GRANDCHILDREN,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX:IN:1;NV;0,s53RFfXA75f[0].LauCl9aicLX:IN:1;NV;0,HI9Y7BKVNnC,JQC3DLdCWK8:GE:0,dkaVGV1WUCR,lXDpHyE8wgb&headers=ouname,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX,s53RFfXA75f[0].LauCl9aicLX,HI9Y7BKVNnC,JQC3DLdCWK8,dkaVGV1WUCR,lXDpHyE8wgb&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
    "numberOfRequests": {
        "total": "17",
        "ok": "17",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "371",
        "ok": "371",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1694",
        "ok": "1694",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "880",
        "ok": "880",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "271",
        "ok": "271",
        "ko": "-"
    },
    "percentiles1": {
        "total": "941",
        "ok": "941",
        "ko": "-"
    },
    "percentiles2": {
        "total": "971",
        "ok": "971",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1178",
        "ok": "1178",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1591",
        "ok": "1591",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 6,
    "percentage": 35.294117647058826
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 10,
    "percentage": 58.82352941176471
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 1,
    "percentage": 5.88235294117647
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "1.06",
        "ok": "1.06",
        "ko": "-"
    }
}
    },"req_-api-analytics---2094250091": {
        type: "REQUEST",
        name: "/api/analytics/enrollments/query/YcxRnVWkbQ1?dimension=ou:IWp9dQGM0bS,sB1IHYu2xQT,ENRjVGxVL6l,oindugucx72,GIb6Ge9PCc4.pXGhYbf5cAP,GIb6Ge9PCc4.LNRDN39NSfY,G0ePNuYPT87.fkqcoDLvzED,cSkxPgrdUKE.kBBrPq9mMEw,F9CrPrvrtb1.bX9jmncMHLC:IN:MEASLES,r9R6DTQdVgR.BLiSlEmoEQH&headers=enrollmentdate,ouname,sB1IHYu2xQT,ENRjVGxVL6l,oindugucx72,GIb6Ge9PCc4.pXGhYbf5cAP,GIb6Ge9PCc4.LNRDN39NSfY,G0ePNuYPT87.fkqcoDLvzED,cSkxPgrdUKE.kBBrPq9mMEw,F9CrPrvrtb1.bX9jmncMHLC,r9R6DTQdVgR.BLiSlEmoEQH&totalPages=false&enrollmentDate=LAST_YEAR&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
path: "/api/analytics/enrollments/query/YcxRnVWkbQ1?dimension=ou:IWp9dQGM0bS,sB1IHYu2xQT,ENRjVGxVL6l,oindugucx72,GIb6Ge9PCc4.pXGhYbf5cAP,GIb6Ge9PCc4.LNRDN39NSfY,G0ePNuYPT87.fkqcoDLvzED,cSkxPgrdUKE.kBBrPq9mMEw,F9CrPrvrtb1.bX9jmncMHLC:IN:MEASLES,r9R6DTQdVgR.BLiSlEmoEQH&headers=enrollmentdate,ouname,sB1IHYu2xQT,ENRjVGxVL6l,oindugucx72,GIb6Ge9PCc4.pXGhYbf5cAP,GIb6Ge9PCc4.LNRDN39NSfY,G0ePNuYPT87.fkqcoDLvzED,cSkxPgrdUKE.kBBrPq9mMEw,F9CrPrvrtb1.bX9jmncMHLC,r9R6DTQdVgR.BLiSlEmoEQH&totalPages=false&enrollmentDate=LAST_YEAR&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
pathFormatted: "req_-api-analytics---2094250091",
stats: {
    "name": "/api/analytics/enrollments/query/YcxRnVWkbQ1?dimension=ou:IWp9dQGM0bS,sB1IHYu2xQT,ENRjVGxVL6l,oindugucx72,GIb6Ge9PCc4.pXGhYbf5cAP,GIb6Ge9PCc4.LNRDN39NSfY,G0ePNuYPT87.fkqcoDLvzED,cSkxPgrdUKE.kBBrPq9mMEw,F9CrPrvrtb1.bX9jmncMHLC:IN:MEASLES,r9R6DTQdVgR.BLiSlEmoEQH&headers=enrollmentdate,ouname,sB1IHYu2xQT,ENRjVGxVL6l,oindugucx72,GIb6Ge9PCc4.pXGhYbf5cAP,GIb6Ge9PCc4.LNRDN39NSfY,G0ePNuYPT87.fkqcoDLvzED,cSkxPgrdUKE.kBBrPq9mMEw,F9CrPrvrtb1.bX9jmncMHLC,r9R6DTQdVgR.BLiSlEmoEQH&totalPages=false&enrollmentDate=LAST_YEAR&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
    "numberOfRequests": {
        "total": "12",
        "ok": "12",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "1052",
        "ok": "1052",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1536",
        "ok": "1536",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "1313",
        "ok": "1313",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "163",
        "ok": "163",
        "ko": "-"
    },
    "percentiles1": {
        "total": "1302",
        "ok": "1302",
        "ko": "-"
    },
    "percentiles2": {
        "total": "1436",
        "ok": "1436",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1533",
        "ok": "1533",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1535",
        "ok": "1535",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0.0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 3,
    "percentage": 25.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 9,
    "percentage": 75.0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.75",
        "ok": "0.75",
        "ko": "-"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
