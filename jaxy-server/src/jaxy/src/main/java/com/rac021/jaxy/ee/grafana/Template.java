
package com.rac021.jaxy.ee.grafana ;

import java.util.List ;
import java.util.Arrays ;

/**
 *
 * @author ryahiaoui
 */

public class Template                                     {

    public final static String DS_NAME  = "JAXY_DS"       ;

    public final static String GridPosH = "{{gridPosH}}"  ;

    public final static String GridPosW = "{{gridPosW}}"  ;

    public final static String GridPosX = "{{gridPosX}}"  ;

    public final static String GridPosY = "{{gridPosY}}"  ;

    public final static String ID       = "{{ID}}"        ;

    public final static String UUID     = "{{UUID}}"      ;

    public final static String NODE_TEMPLATE      = "{{NodeTemplate}}"       ;

    public final static String SNAKE_SERVICE_NAME = "{{SNAKE_SERVICE_NAME}}" ;

    public final static String PROMETHEUS_URL     = "localhost"              ;

    public final static String TITLE_COUNTERS     = "Jaxy_Services_Counters" ;
    
    public final static String TITLE_TIMERS       = "Jaxy_Services_Timers"   ;
    
    public final static String TITLE              = "{{TITLE}}"              ;
    
    public final static String COLOR_TEMPLATE     = "{{COLOR_TEMPLATE}}"     ;
    
    public final static String SNAKE_SERVICE_NAME_FROM_PROMETHEUS = "{{SNAKE_SERVICE_NAME_FROM_PROMETHEUS}}" ;

    
    public static String DashBoard =
            
            "{\n" + "  \"__inputs\": [\n" + "    {\n" + "      \"name\": \"" + DS_NAME
            + "\",\n" + "      \"label\": \"Jaxy-source\",\n" + "      \"description\": \"\",\n"
            + "      \"type\": \"datasource\",\n" + "      \"pluginId\": \"prometheus\",\n"
            + "      \"pluginName\": \"Prometheus\"\n" + "    }\n" + "  ],\n" + "  \"__requires\": [\n" + "    {\n"
            + "      \"type\": \"grafana\",\n" + "      \"id\": \"grafana\",\n" + "      \"name\": \"Grafana\",\n"
            + "      \"version\": \"5.2.1\"\n" + "    },\n" + "    {\n" + "      \"type\": \"datasource\",\n"
            + "      \"id\": \"prometheus\",\n" + "      \"name\": \"Prometheus\",\n" + "      \"version\": \"5.0.0\"\n"
            + "    },\n" + "    {\n" + "      \"type\": \"panel\",\n" + "      \"id\": \"singlestat\",\n"
            + "      \"name\": \"Singlestat\",\n" + "      \"version\": \"5.0.0\"\n" + "    }\n" + "  ],\n"
            + "  \"annotations\": {\n" + "    \"list\": [\n" + "      {\n" + "        \"builtIn\": 1,\n"
            + "        \"datasource\": \"-- Grafana --\",\n" + "        \"enable\": true,\n"
            + "        \"hide\": true,\n" + "        \"iconColor\": \"rgba(0, 211, 255, 1)\",\n"
            + "        \"name\": \"Annotations & Alerts\",\n" + "        \"type\": \"dashboard\"\n" + "      }\n"
            + "    ]\n" + "  },\n" + "  \"editable\": true,\n" + "  \"gnetId\": null,\n" + "  \"graphTooltip\": 0,\n"
            + "  \"id\": null,\n" + "  \"links\": [],\n" + "  \"panels\": [ \n" + " " + NODE_TEMPLATE + " " + "  ],"
            + "  \"refresh\": \"1s\",\n" + "  \"schemaVersion\": 16,\n" + "  \"style\": \"dark\",\n"
            + "  \"tags\": [],\n" + "  \"templating\": {\n" + "    \"list\": []\n" + "  },\n" + "  \"time\": {\n"
            + "    \"from\": \"now-1m\",\n" + "    \"to\": \"now\"\n" + "  },\n" + "  \"timepicker\": {\n"
            + "    \"hidden\": false,\n" + "    \"nowDelay\": \"1m\",\n" + "    \"refresh_intervals\": [\n"
            + "      \"1s\"\n" + "    ],\n" + "    \"time_options\": [\n" + "      \"5m\",\n" + "      \"15m\",\n"
            + "      \"1h\",\n" + "      \"6h\",\n" + "      \"12h\",\n" + "      \"24h\",\n" + "      \"2d\",\n"
            + "      \"7d\",\n" + "      \"30d\"\n" + "    ]\n" + "  },\n" + "  \"timezone\": \"\",\n"
            + "  \"title\": \"" + TITLE + "\",\n" + "  \"uid\": \"" + UUID + "\",\n" + "  \"version\": 8\n" + "}";

    
    public static String ServiceCounterTemplate =
            
            "{\n" + "      \"cacheTimeout\": null,\n"
            + "      \"colorBackground\": true,\n" + "      \"colorValue\": false,\n" + "      \"colors\": [\n"
            + "        \""+ COLOR_TEMPLATE + "\",\n" + "        \"rgba(237, 129, 40, 0.89)\",\n" + "        \"#299c46\"\n"
            + "      ],\n" + "      \"datasource\": \"" + DS_NAME + "\",\n" + "      \"format\": \"none\",\n"
            + "      \"gauge\": {\n" + "        \"maxValue\": 100000000,\n" + "        \"minValue\": 0,\n"
            + "        \"show\": true,\n" + "        \"thresholdLabels\": false,\n"
            + "        \"thresholdMarkers\": true\n" + "      },\n" + "      \"gridPos\": {\n" + "        \"h\": "
            + GridPosH + ",\n" + "        \"w\": " + GridPosW + ",\n" + "        \"x\": " + GridPosX + ",\n"
            + "        \"y\": " + GridPosY + " \n" + "      },\n" + "      \"hideTimeOverride\": false,\n"
            + "      \"id\": " + ID + ",\n" + "      \"interval\": \"1s\",\n" + "      \"links\": [],\n"
            + "      \"mappingType\": 1,\n" + "      \"mappingTypes\": [\n" + "        {\n"
            + "          \"name\": \"value to text\",\n" + "          \"value\": 1\n" + "        },\n" + "        {\n"
            + "          \"name\": \"range to text\",\n" + "          \"value\": 2\n" + "        }\n" + "      ],\n"
            + "      \"maxDataPoints\": 100,\n" + "      \"nullPointMode\": \"connected\",\n"
            + "      \"nullText\": null,\n" + "      \"postfix\": \"\",\n" + "      \"postfixFontSize\": \"50%\",\n"
            + "      \"prefix\": \"\",\n" + "      \"prefixFontSize\": \"150%\",\n" + "      \"rangeMaps\": [\n"
            + "        {\n" + "          \"from\": \"null\",\n" + "          \"text\": \"N/A\",\n"
            + "          \"to\": \"null\"\n" + "        }\n" + "      ],\n" + "      \"repeat\": null,\n"
            + "      \"sparkline\": {\n" + "        \"fillColor\": \"rgba(31, 118, 189, 0.18)\",\n"
            + "        \"full\": true,\n" + "        \"lineColor\": \"#629e51\",\n" + "        \"show\": true\n"
            + "      },\n" + "      \"tableColumn\": \"__name__\",\n" + "      \"targets\": [\n" + "        {\n"
            + "          \"expr\": \"application_" + SNAKE_SERVICE_NAME_FROM_PROMETHEUS + "\",\n"
            + "          \"format\": \"time_series\",\n" + "          \"hide\": false,\n"
            + "          \"instant\": true,\n" + "          \"interval\": \"\",\n"
            + "          \"intervalFactor\": 2,\n" + "          \"legendFormat\": \"" + SNAKE_SERVICE_NAME + "\",\n"
            + "          \"refId\": \"A\"\n" + "        }\n" + "      ],\n" + "      \"thresholds\": \"10000000\",\n"
            + "      \"timeFrom\": \"0h\",\n" + "      \"timeShift\": \"0h\",\n" + "      \"title\": \""
            + SNAKE_SERVICE_NAME + "\",\n" + "      \"transparent\": true,\n" + "      \"type\": \"singlestat\",\n"
            + "      \"valueFontSize\": \"200%\",\n" + "      \"valueMaps\": [\n" + "        {\n"
            + "          \"op\": \"=\",\n" + "          \"text\": \"N/A\",\n" + "          \"value\": \"null\"\n"
            + "        }\n" + "      ],\n" + "      \"valueName\": \"total\"\n" + "    }";


    public static String ServiceTimerTemplate = 
            
            "{\n" + "      \"cacheTimeout\": null,\n"
            + "      \"colorBackground\": true,\n" + "      \"colorValue\": false,\n" + "      \"colors\": [\n"
            + "        \""+ COLOR_TEMPLATE + "\",\n" + "        \"rgba(237, 129, 40, 0.89)\",\n" + "        \"#299c46\"\n"
            + "      ],\n" + "      \"datasource\": \"" + DS_NAME + "\",\n" + "      \"format\": \"s\",\n"
            + "      \n" + "      \"gridPos\": {\n" + "        \"h\": "
            +        GridPosH + ",\n" + "        \"w\": " + GridPosW + ",\n" + "        \"x\": " + GridPosX + ",\n"
            + "        \"y\": " + GridPosY + " \n" + "      },\n" + "      \"hideTimeOverride\": false,\n"
            + "      \"id\": " + ID + ",\n" + "      \"interval\": \"1s\",\n" + "      \"links\": [],\n"
            + "      \"mappingType\": 1,\n" + "      \"mappingTypes\": [\n" + "        {\n"
            + "          \"name\": \"value to text\",\n" + "          \"value\": 1\n" + "        },\n" + "        {\n"
            + "          \"name\": \"range to text\",\n" + "          \"value\": 2\n" + "        }\n" + "      ],\n"
            + "      \"maxDataPoints\": 100,\n" + "      \"nullPointMode\": \"connected\",\n"
            + "      \"nullText\": null,\n" + "      \"postfix\": \"\",\n" + "      \"postfixFontSize\": \"50%\",\n"
            + "      \"prefix\": \"\",\n" + "      \"prefixFontSize\": \"150%\",\n" + "      \"rangeMaps\": [\n"
            + "        {\n" + "          \"from\": \"null\",\n" + "          \"text\": \"N/A\",\n"
            + "          \"to\": \"null\"\n" + "        }\n" + "      ],\n" + "      \"repeat\": null,\n"
            + "      \"sparkline\": {\n" + "        \"fillColor\": \"rgba(31, 118, 189, 0.18)\",\n"
            + "        \"full\": true,\n" + "        \"lineColor\": \"#629e51\",\n" + "        \"show\": true\n"
            + "      },\n" + "      \"tableColumn\": \"__name__\",\n" + "      \"targets\": [\n" + "        {\n"
            + "          \"expr\": \"application_" + SNAKE_SERVICE_NAME_FROM_PROMETHEUS + "\",\n"
            + "          \"format\": \"time_series\",\n" + "          \"hide\": false,\n"
            + "          \"instant\": true,\n" + "          \"interval\": \"\",\n"
            + "          \"intervalFactor\": 2,\n" + "          \"legendFormat\": \"" + SNAKE_SERVICE_NAME + "\",\n"
            + "          \"refId\": \"A\"\n" + "        }\n" + "      ],\n" + "      \"thresholds\": \"10000000\",\n"
            + "      \"timeFrom\": \"0h\",\n" + "      \"timeShift\": \"0h\",\n" + "      \"title\": \""
            +        SNAKE_SERVICE_NAME + "\",\n" + "      \"transparent\": true,\n" + "      \"type\": \"singlestat\",\n"
            + "      \"valueFontSize\": \"200%\",\n" + "      \"valueMaps\": [\n" + "        {\n"
            + "          \"op\": \"=\",\n" + "          \"text\": \"N/A\",\n" + "          \"value\": \"null\"\n"
            + "        }\n" + "      ],\n" + "      \"valueName\": \"avg\"\n" + "    }";

    
    public static String DataSourceYml = 
            
            "# config file version\n" + "apiVersion: 1\n" + "\n"
            + "# list of datasources that should be deleted from the database\n" + "deleteDatasources:\n"
            + "  - name: Prometheus\n" + "    orgId: 1\n" + "\n" + "# list of datasources to insert/update depending\n"
            + "# whats available in the database\n" + "datasources:\n"
            + "  # <string, required> name of the datasource. Required\n" + "- name: " + DS_NAME + "\n"
            + "  # <string, required> datasource type. Required\n" + "  type: prometheus\n"
            + "  # <string, required> access mode. direct or proxy. Required\n" + "  access: proxy\n"
            + "  # <int> org id. will default to orgId 1 if not specified\n" + "  orgId: 1\n" + "  # <string> url\n"
            + "  url: http://" + PROMETHEUS_URL + ":9090\n" + "  # <string> database password, if used\n"
            + "  password:\n" + "  # <string> database user, if used\n" + "  user:\n"
            + "  # <string> database name, if used\n" + "  database:\n" + "  # <bool> enable/disable basic auth\n"
            + "  basicAuth: true\n" + "  # <string> basic auth username\n" + "  basicAuthUser: admin\n"
            + "  # <string> basic auth password\n" + "  basicAuthPassword: foobar\n"
            + "  # <bool> enable/disable with credentials headers\n" + "  withCredentials:\n"
            + "  # <bool> mark as default datasource. Max one per org\n" + "  isDefault:\n"
            + "  # <map> fields that will be converted to json and stored in json_data\n" + "  jsonData:\n"
            + "     graphiteVersion: \"1.1\"\n" + "     tlsAuth: false\n" + "     tlsAuthWithCACert: false\n"
            + "  # <string> json object of data that will be encrypted.\n" + "  secureJsonData:\n"
            + "    tlsCACert: \"...\"\n" + "    tlsClientCert: \"...\"\n" + "    tlsClientKey: \"...\"\n"
            + "  version: 1\n" + "  # <bool> allow users to edit datasources from the UI.\n" + "  editable: true";

    
    /** Random Colors In Grafana . **/
    
    public static List<String> colors = Arrays.asList ( 
                                                       "#0a437c"       ,
                                                       "#3f6833"       ,
                                                       "#967302"       ,
                                                       "#705da0"       ,
                                                       "#ef843c"       ,
                                                       "#ba43a9"       ,
                                                       "#9C640C"       ,
                                                       "rgb(3, 42, 27)",
                                                       "#5195ce"       ,
                                                       "#58140c"       ,
                                                       "#e24d42"       ,
                                                       "#3f6833"       ,
                                                       "#052b51"       ,
                                                       "#c15c17"       ,
                                                       "#aea2e0"       ,
                                                       "#7eb26d"       ,
                                                       "#2e4043"       ,
                                                       "#511749" 
                                                     ) ;
}

