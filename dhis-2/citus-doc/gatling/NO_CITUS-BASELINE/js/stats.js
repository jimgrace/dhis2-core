var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name--1146707516",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "198",
        "ok": "198",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "145",
        "ok": "145",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "51853",
        "ok": "51853",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "727",
        "ok": "727",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "3758",
        "ok": "3758",
        "ko": "-"
    },
    "percentiles1": {
        "total": "282",
        "ok": "282",
        "ko": "-"
    },
    "percentiles2": {
        "total": "355",
        "ok": "355",
        "ko": "-"
    },
    "percentiles3": {
        "total": "748",
        "ok": "748",
        "ko": "-"
    },
    "percentiles4": {
        "total": "5125",
        "ok": "5125",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 188,
    "percentage": 94.94949494949495
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 10,
    "percentage": 5.05050505050505
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "3.81",
        "ok": "3.81",
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
        "total": "35",
        "ok": "35",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "224",
        "ok": "224",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "4730",
        "ok": "4730",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "432",
        "ok": "432",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "740",
        "ok": "740",
        "ko": "-"
    },
    "percentiles1": {
        "total": "285",
        "ok": "285",
        "ko": "-"
    },
    "percentiles2": {
        "total": "358",
        "ok": "358",
        "ko": "-"
    },
    "percentiles3": {
        "total": "474",
        "ok": "474",
        "ko": "-"
    },
    "percentiles4": {
        "total": "3283",
        "ok": "3283",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 34,
    "percentage": 97.14285714285714
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 1,
    "percentage": 2.857142857142857
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.67",
        "ok": "0.67",
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
        "total": "27",
        "ok": "27",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "282",
        "ok": "282",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "5006",
        "ok": "5006",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "556",
        "ok": "556",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "877",
        "ok": "877",
        "ko": "-"
    },
    "percentiles1": {
        "total": "343",
        "ok": "343",
        "ko": "-"
    },
    "percentiles2": {
        "total": "456",
        "ok": "456",
        "ko": "-"
    },
    "percentiles3": {
        "total": "579",
        "ok": "579",
        "ko": "-"
    },
    "percentiles4": {
        "total": "3858",
        "ok": "3858",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 26,
    "percentage": 96.29629629629629
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 1,
    "percentage": 3.7037037037037033
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.52",
        "ok": "0.52",
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
        "total": "40",
        "ok": "40",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "198",
        "ok": "198",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "4543",
        "ok": "4543",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "378",
        "ok": "378",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "671",
        "ok": "671",
        "ko": "-"
    },
    "percentiles1": {
        "total": "246",
        "ok": "246",
        "ko": "-"
    },
    "percentiles2": {
        "total": "290",
        "ok": "290",
        "ko": "-"
    },
    "percentiles3": {
        "total": "465",
        "ok": "465",
        "ko": "-"
    },
    "percentiles4": {
        "total": "2979",
        "ok": "2979",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 39,
    "percentage": 97.5
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 1,
    "percentage": 2.5
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.77",
        "ok": "0.77",
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
        "total": "1",
        "ok": "1",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "51853",
        "ok": "51853",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "51853",
        "ok": "51853",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "51853",
        "ok": "51853",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "0",
        "ok": "0",
        "ko": "-"
    },
    "percentiles1": {
        "total": "51853",
        "ok": "51853",
        "ko": "-"
    },
    "percentiles2": {
        "total": "51853",
        "ok": "51853",
        "ko": "-"
    },
    "percentiles3": {
        "total": "51853",
        "ok": "51853",
        "ko": "-"
    },
    "percentiles4": {
        "total": "51853",
        "ok": "51853",
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
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 1,
    "percentage": 100.0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.02",
        "ok": "0.02",
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
        "total": "44",
        "ok": "44",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "190",
        "ok": "190",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1632",
        "ok": "1632",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "341",
        "ok": "341",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "214",
        "ok": "214",
        "ko": "-"
    },
    "percentiles1": {
        "total": "297",
        "ok": "297",
        "ko": "-"
    },
    "percentiles2": {
        "total": "372",
        "ok": "372",
        "ko": "-"
    },
    "percentiles3": {
        "total": "472",
        "ok": "472",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1163",
        "ok": "1163",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 43,
    "percentage": 97.72727272727273
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 1,
    "percentage": 2.272727272727273
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.85",
        "ok": "0.85",
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
        "total": "47",
        "ok": "47",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "145",
        "ok": "145",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "4729",
        "ok": "4729",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "319",
        "ok": "319",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "653",
        "ok": "653",
        "ko": "-"
    },
    "percentiles1": {
        "total": "207",
        "ok": "207",
        "ko": "-"
    },
    "percentiles2": {
        "total": "242",
        "ok": "242",
        "ko": "-"
    },
    "percentiles3": {
        "total": "387",
        "ok": "387",
        "ko": "-"
    },
    "percentiles4": {
        "total": "2744",
        "ok": "2744",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 46,
    "percentage": 97.87234042553192
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 1,
    "percentage": 2.127659574468085
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.9",
        "ok": "0.9",
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
        "total": "4",
        "ok": "4",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "1920",
        "ok": "1920",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "8975",
        "ok": "8975",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "4229",
        "ok": "4229",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "2776",
        "ok": "2776",
        "ko": "-"
    },
    "percentiles1": {
        "total": "3011",
        "ok": "3011",
        "ko": "-"
    },
    "percentiles2": {
        "total": "4525",
        "ok": "4525",
        "ko": "-"
    },
    "percentiles3": {
        "total": "8085",
        "ok": "8085",
        "ko": "-"
    },
    "percentiles4": {
        "total": "8797",
        "ok": "8797",
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
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 4,
    "percentage": 100.0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.08",
        "ok": "0.08",
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
