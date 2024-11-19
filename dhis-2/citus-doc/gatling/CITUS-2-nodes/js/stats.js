var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name--1146707516",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "15",
        "ok": "15",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "5958",
        "ok": "5958",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "25165",
        "ok": "25165",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "10868",
        "ok": "10868",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "4951",
        "ok": "4951",
        "ko": "-"
    },
    "percentiles1": {
        "total": "9935",
        "ok": "9935",
        "ko": "-"
    },
    "percentiles2": {
        "total": "13464",
        "ok": "13464",
        "ko": "-"
    },
    "percentiles3": {
        "total": "17974",
        "ok": "17974",
        "ko": "-"
    },
    "percentiles4": {
        "total": "23727",
        "ok": "23727",
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
    "count": 15,
    "percentage": 100.0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.58",
        "ok": "0.58",
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
        "total": "2",
        "ok": "2",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "7196",
        "ok": "7196",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "14892",
        "ok": "14892",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "11044",
        "ok": "11044",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "3848",
        "ok": "3848",
        "ko": "-"
    },
    "percentiles1": {
        "total": "11044",
        "ok": "11044",
        "ko": "-"
    },
    "percentiles2": {
        "total": "12968",
        "ok": "12968",
        "ko": "-"
    },
    "percentiles3": {
        "total": "14507",
        "ok": "14507",
        "ko": "-"
    },
    "percentiles4": {
        "total": "14815",
        "ok": "14815",
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
    "count": 2,
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
    },"req_-api-analytics--1703293855": {
        type: "REQUEST",
        name: "/api/analytics/enrollments/query/M3xtLkYBlKI?dimension=ou:IWp9dQGM0bS;W6sNfkJcXGC;LEVEL-b5jE033nBqM;LEVEL-vFr4zVw6Avn;OU_GROUP-YXlxwXEWex6;OU_GROUP-roGQQw4l3dW;OU_GROUP-VePuVPFoyJ2,cl2RC5MLQYO:GE:0,gDgZ5oXCyWm,DishKl0ppXK,nO7nsEjYcp5,zyTL3AMIkf2,OHeZKzifNYE,d6Sr0B2NJYv,yZmG3RbbBKG,uvMKOn1oWvd.yhX7ljWZV9q:IN:NV,uvMKOn1oWvd.JhpYDsTUfi2:IN:1,CWaAcQYKVpq[1].dbMsAGvictz,CWaAcQYKVpq[2].dbMsAGvictz,CWaAcQYKVpq[0].dbMsAGvictz,CWaAcQYKVpq.ehBd9cR5bq4:EQ:NV,CWaAcQYKVpq.VNM6zoPECqd:GT:0,CWaAcQYKVpq.SaHE38QFFwZ:IN:HILLY_AND_PLATUE;PLATUE;HILLY&headers=ouname,cl2RC5MLQYO,gDgZ5oXCyWm,DishKl0ppXK,nO7nsEjYcp5,zyTL3AMIkf2,OHeZKzifNYE,d6Sr0B2NJYv,yZmG3RbbBKG,uvMKOn1oWvd.yhX7ljWZV9q,uvMKOn1oWvd.JhpYDsTUfi2,CWaAcQYKVpq[1].dbMsAGvictz,CWaAcQYKVpq[2].dbMsAGvictz,CWaAcQYKVpq[0].dbMsAGvictz,CWaAcQYKVpq.ehBd9cR5bq4,CWaAcQYKVpq.VNM6zoPECqd,CWaAcQYKVpq.SaHE38QFFwZ,createdbydisplayname&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&programStatus=COMPLETED,ACTIVE&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
path: "/api/analytics/enrollments/query/M3xtLkYBlKI?dimension=ou:IWp9dQGM0bS;W6sNfkJcXGC;LEVEL-b5jE033nBqM;LEVEL-vFr4zVw6Avn;OU_GROUP-YXlxwXEWex6;OU_GROUP-roGQQw4l3dW;OU_GROUP-VePuVPFoyJ2,cl2RC5MLQYO:GE:0,gDgZ5oXCyWm,DishKl0ppXK,nO7nsEjYcp5,zyTL3AMIkf2,OHeZKzifNYE,d6Sr0B2NJYv,yZmG3RbbBKG,uvMKOn1oWvd.yhX7ljWZV9q:IN:NV,uvMKOn1oWvd.JhpYDsTUfi2:IN:1,CWaAcQYKVpq[1].dbMsAGvictz,CWaAcQYKVpq[2].dbMsAGvictz,CWaAcQYKVpq[0].dbMsAGvictz,CWaAcQYKVpq.ehBd9cR5bq4:EQ:NV,CWaAcQYKVpq.VNM6zoPECqd:GT:0,CWaAcQYKVpq.SaHE38QFFwZ:IN:HILLY_AND_PLATUE;PLATUE;HILLY&headers=ouname,cl2RC5MLQYO,gDgZ5oXCyWm,DishKl0ppXK,nO7nsEjYcp5,zyTL3AMIkf2,OHeZKzifNYE,d6Sr0B2NJYv,yZmG3RbbBKG,uvMKOn1oWvd.yhX7ljWZV9q,uvMKOn1oWvd.JhpYDsTUfi2,CWaAcQYKVpq[1].dbMsAGvictz,CWaAcQYKVpq[2].dbMsAGvictz,CWaAcQYKVpq[0].dbMsAGvictz,CWaAcQYKVpq.ehBd9cR5bq4,CWaAcQYKVpq.VNM6zoPECqd,CWaAcQYKVpq.SaHE38QFFwZ,createdbydisplayname&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&programStatus=COMPLETED,ACTIVE&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
pathFormatted: "req_-api-analytics--1703293855",
stats: {
    "name": "/api/analytics/enrollments/query/M3xtLkYBlKI?dimension=ou:IWp9dQGM0bS;W6sNfkJcXGC;LEVEL-b5jE033nBqM;LEVEL-vFr4zVw6Avn;OU_GROUP-YXlxwXEWex6;OU_GROUP-roGQQw4l3dW;OU_GROUP-VePuVPFoyJ2,cl2RC5MLQYO:GE:0,gDgZ5oXCyWm,DishKl0ppXK,nO7nsEjYcp5,zyTL3AMIkf2,OHeZKzifNYE,d6Sr0B2NJYv,yZmG3RbbBKG,uvMKOn1oWvd.yhX7ljWZV9q:IN:NV,uvMKOn1oWvd.JhpYDsTUfi2:IN:1,CWaAcQYKVpq[1].dbMsAGvictz,CWaAcQYKVpq[2].dbMsAGvictz,CWaAcQYKVpq[0].dbMsAGvictz,CWaAcQYKVpq.ehBd9cR5bq4:EQ:NV,CWaAcQYKVpq.VNM6zoPECqd:GT:0,CWaAcQYKVpq.SaHE38QFFwZ:IN:HILLY_AND_PLATUE;PLATUE;HILLY&headers=ouname,cl2RC5MLQYO,gDgZ5oXCyWm,DishKl0ppXK,nO7nsEjYcp5,zyTL3AMIkf2,OHeZKzifNYE,d6Sr0B2NJYv,yZmG3RbbBKG,uvMKOn1oWvd.yhX7ljWZV9q,uvMKOn1oWvd.JhpYDsTUfi2,CWaAcQYKVpq[1].dbMsAGvictz,CWaAcQYKVpq[2].dbMsAGvictz,CWaAcQYKVpq[0].dbMsAGvictz,CWaAcQYKVpq.ehBd9cR5bq4,CWaAcQYKVpq.VNM6zoPECqd,CWaAcQYKVpq.SaHE38QFFwZ,createdbydisplayname&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&programStatus=COMPLETED,ACTIVE&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
    "numberOfRequests": {
        "total": "3",
        "ok": "3",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "5958",
        "ok": "5958",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "10330",
        "ok": "10330",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "7532",
        "ok": "7532",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "1984",
        "ok": "1984",
        "ko": "-"
    },
    "percentiles1": {
        "total": "6307",
        "ok": "6307",
        "ko": "-"
    },
    "percentiles2": {
        "total": "8319",
        "ok": "8319",
        "ko": "-"
    },
    "percentiles3": {
        "total": "9928",
        "ok": "9928",
        "ko": "-"
    },
    "percentiles4": {
        "total": "10250",
        "ok": "10250",
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
    "count": 3,
    "percentage": 100.0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.12",
        "ok": "0.12",
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
        "total": "3",
        "ok": "3",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "5974",
        "ok": "5974",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "9935",
        "ok": "9935",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "7521",
        "ok": "7521",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "1729",
        "ok": "1729",
        "ko": "-"
    },
    "percentiles1": {
        "total": "6655",
        "ok": "6655",
        "ko": "-"
    },
    "percentiles2": {
        "total": "8295",
        "ok": "8295",
        "ko": "-"
    },
    "percentiles3": {
        "total": "9607",
        "ok": "9607",
        "ko": "-"
    },
    "percentiles4": {
        "total": "9869",
        "ok": "9869",
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
    "count": 3,
    "percentage": 100.0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.12",
        "ok": "0.12",
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
        "total": "2",
        "ok": "2",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "9731",
        "ok": "9731",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "13861",
        "ok": "13861",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "11796",
        "ok": "11796",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "2065",
        "ok": "2065",
        "ko": "-"
    },
    "percentiles1": {
        "total": "11796",
        "ok": "11796",
        "ko": "-"
    },
    "percentiles2": {
        "total": "12829",
        "ok": "12829",
        "ko": "-"
    },
    "percentiles3": {
        "total": "13655",
        "ok": "13655",
        "ko": "-"
    },
    "percentiles4": {
        "total": "13820",
        "ok": "13820",
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
    "count": 2,
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
    },"req_-api-analytics---741163252": {
        type: "REQUEST",
        name: "/api/analytics/enrollments/query/KYzHf1Ta6C4?dimension=ou:USER_ORGUNIT,BdvE9shT6GX,bE3YcdNxA3g:GT:1&headers=enrollmentdate,ouname,BdvE9shT6GX,bE3YcdNxA3g&totalPages=false&enrollmentDate=THIS_YEAR,LAST_YEAR&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
path: "/api/analytics/enrollments/query/KYzHf1Ta6C4?dimension=ou:USER_ORGUNIT,BdvE9shT6GX,bE3YcdNxA3g:GT:1&headers=enrollmentdate,ouname,BdvE9shT6GX,bE3YcdNxA3g&totalPages=false&enrollmentDate=THIS_YEAR,LAST_YEAR&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
pathFormatted: "req_-api-analytics---741163252",
stats: {
    "name": "/api/analytics/enrollments/query/KYzHf1Ta6C4?dimension=ou:USER_ORGUNIT,BdvE9shT6GX,bE3YcdNxA3g:GT:1&headers=enrollmentdate,ouname,BdvE9shT6GX,bE3YcdNxA3g&totalPages=false&enrollmentDate=THIS_YEAR,LAST_YEAR&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
    "numberOfRequests": {
        "total": "2",
        "ok": "2",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "11879",
        "ok": "11879",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "13067",
        "ok": "13067",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "12473",
        "ok": "12473",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "594",
        "ok": "594",
        "ko": "-"
    },
    "percentiles1": {
        "total": "12473",
        "ok": "12473",
        "ko": "-"
    },
    "percentiles2": {
        "total": "12770",
        "ok": "12770",
        "ko": "-"
    },
    "percentiles3": {
        "total": "13008",
        "ok": "13008",
        "ko": "-"
    },
    "percentiles4": {
        "total": "13055",
        "ok": "13055",
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
    "count": 2,
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
    },"req_-api-analytics--238133834": {
        type: "REQUEST",
        name: "/api/analytics/enrollments/query/SSLpOM0r1U7?dimension=ou:USER_ORGUNIT;USER_ORGUNIT_CHILDREN;USER_ORGUNIT_GRANDCHILDREN,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX:IN:1;NV;0,s53RFfXA75f[0].LauCl9aicLX:IN:1;NV;0,HI9Y7BKVNnC,JQC3DLdCWK8:GE:0,dkaVGV1WUCR,lXDpHyE8wgb&headers=ouname,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX,s53RFfXA75f[0].LauCl9aicLX,HI9Y7BKVNnC,JQC3DLdCWK8,dkaVGV1WUCR,lXDpHyE8wgb&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
path: "/api/analytics/enrollments/query/SSLpOM0r1U7?dimension=ou:USER_ORGUNIT;USER_ORGUNIT_CHILDREN;USER_ORGUNIT_GRANDCHILDREN,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX:IN:1;NV;0,s53RFfXA75f[0].LauCl9aicLX:IN:1;NV;0,HI9Y7BKVNnC,JQC3DLdCWK8:GE:0,dkaVGV1WUCR,lXDpHyE8wgb&headers=ouname,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX,s53RFfXA75f[0].LauCl9aicLX,HI9Y7BKVNnC,JQC3DLdCWK8,dkaVGV1WUCR,lXDpHyE8wgb&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
pathFormatted: "req_-api-analytics--238133834",
stats: {
    "name": "/api/analytics/enrollments/query/SSLpOM0r1U7?dimension=ou:USER_ORGUNIT;USER_ORGUNIT_CHILDREN;USER_ORGUNIT_GRANDCHILDREN,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX:IN:1;NV;0,s53RFfXA75f[0].LauCl9aicLX:IN:1;NV;0,HI9Y7BKVNnC,JQC3DLdCWK8:GE:0,dkaVGV1WUCR,lXDpHyE8wgb&headers=ouname,itJqasg1QiC,jMotFa52JAq,s53RFfXA75f.LTdvha8zapG,s53RFfXA75f[-1].LauCl9aicLX,s53RFfXA75f[0].LauCl9aicLX,HI9Y7BKVNnC,JQC3DLdCWK8,dkaVGV1WUCR,lXDpHyE8wgb&totalPages=false&lastUpdated=2021-08-01_2024-11-23&enrollmentDate=LAST_12_MONTHS&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
    "numberOfRequests": {
        "total": "2",
        "ok": "2",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "7184",
        "ok": "7184",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "14884",
        "ok": "14884",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "11034",
        "ok": "11034",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "3850",
        "ok": "3850",
        "ko": "-"
    },
    "percentiles1": {
        "total": "11034",
        "ok": "11034",
        "ko": "-"
    },
    "percentiles2": {
        "total": "12959",
        "ok": "12959",
        "ko": "-"
    },
    "percentiles3": {
        "total": "14499",
        "ok": "14499",
        "ko": "-"
    },
    "percentiles4": {
        "total": "14807",
        "ok": "14807",
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
    "count": 2,
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
    },"req_-api-analytics---2094250091": {
        type: "REQUEST",
        name: "/api/analytics/enrollments/query/YcxRnVWkbQ1?dimension=ou:IWp9dQGM0bS,sB1IHYu2xQT,ENRjVGxVL6l,oindugucx72,GIb6Ge9PCc4.pXGhYbf5cAP,GIb6Ge9PCc4.LNRDN39NSfY,G0ePNuYPT87.fkqcoDLvzED,cSkxPgrdUKE.kBBrPq9mMEw,F9CrPrvrtb1.bX9jmncMHLC:IN:MEASLES,r9R6DTQdVgR.BLiSlEmoEQH&headers=enrollmentdate,ouname,sB1IHYu2xQT,ENRjVGxVL6l,oindugucx72,GIb6Ge9PCc4.pXGhYbf5cAP,GIb6Ge9PCc4.LNRDN39NSfY,G0ePNuYPT87.fkqcoDLvzED,cSkxPgrdUKE.kBBrPq9mMEw,F9CrPrvrtb1.bX9jmncMHLC,r9R6DTQdVgR.BLiSlEmoEQH&totalPages=false&enrollmentDate=LAST_YEAR&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
path: "/api/analytics/enrollments/query/YcxRnVWkbQ1?dimension=ou:IWp9dQGM0bS,sB1IHYu2xQT,ENRjVGxVL6l,oindugucx72,GIb6Ge9PCc4.pXGhYbf5cAP,GIb6Ge9PCc4.LNRDN39NSfY,G0ePNuYPT87.fkqcoDLvzED,cSkxPgrdUKE.kBBrPq9mMEw,F9CrPrvrtb1.bX9jmncMHLC:IN:MEASLES,r9R6DTQdVgR.BLiSlEmoEQH&headers=enrollmentdate,ouname,sB1IHYu2xQT,ENRjVGxVL6l,oindugucx72,GIb6Ge9PCc4.pXGhYbf5cAP,GIb6Ge9PCc4.LNRDN39NSfY,G0ePNuYPT87.fkqcoDLvzED,cSkxPgrdUKE.kBBrPq9mMEw,F9CrPrvrtb1.bX9jmncMHLC,r9R6DTQdVgR.BLiSlEmoEQH&totalPages=false&enrollmentDate=LAST_YEAR&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
pathFormatted: "req_-api-analytics---2094250091",
stats: {
    "name": "/api/analytics/enrollments/query/YcxRnVWkbQ1?dimension=ou:IWp9dQGM0bS,sB1IHYu2xQT,ENRjVGxVL6l,oindugucx72,GIb6Ge9PCc4.pXGhYbf5cAP,GIb6Ge9PCc4.LNRDN39NSfY,G0ePNuYPT87.fkqcoDLvzED,cSkxPgrdUKE.kBBrPq9mMEw,F9CrPrvrtb1.bX9jmncMHLC:IN:MEASLES,r9R6DTQdVgR.BLiSlEmoEQH&headers=enrollmentdate,ouname,sB1IHYu2xQT,ENRjVGxVL6l,oindugucx72,GIb6Ge9PCc4.pXGhYbf5cAP,GIb6Ge9PCc4.LNRDN39NSfY,G0ePNuYPT87.fkqcoDLvzED,cSkxPgrdUKE.kBBrPq9mMEw,F9CrPrvrtb1.bX9jmncMHLC,r9R6DTQdVgR.BLiSlEmoEQH&totalPages=false&enrollmentDate=LAST_YEAR&displayProperty=NAME&outputType=ENROLLMENT&pageSize=100&page=1&includeMetadataDetails=true&relativePeriodDate=2023-11-14",
    "numberOfRequests": {
        "total": "1",
        "ok": "1",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "25165",
        "ok": "25165",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "25165",
        "ok": "25165",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "25165",
        "ok": "25165",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "0",
        "ok": "0",
        "ko": "-"
    },
    "percentiles1": {
        "total": "25165",
        "ok": "25165",
        "ko": "-"
    },
    "percentiles2": {
        "total": "25165",
        "ok": "25165",
        "ko": "-"
    },
    "percentiles3": {
        "total": "25165",
        "ok": "25165",
        "ko": "-"
    },
    "percentiles4": {
        "total": "25165",
        "ok": "25165",
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
        "total": "0.04",
        "ok": "0.04",
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
