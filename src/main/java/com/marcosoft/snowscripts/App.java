package com.marcosoft.snowscripts;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest.BodyPublishers;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Scanner;

public class App {

    static Scanner userInput = new Scanner(System.in);

    static HttpClient httpClient = HttpClient.newBuilder()
            .authenticator(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            "adm_marcos_strapazon",
                            "Sicredi123".toCharArray());
                }
            })
            .build();

    public static void main( String[] args ){
        Integer selectedOperation = 0;
        String endPoint = "";
        String requestBody = "";
        HttpRequest snowRequest = null;
        writeMenu();
        selectedOperation = userInput.nextInt();

        switch (selectedOperation){
            case 1:
                System.out.println("GET Selected");
                System.out.println("");
                endPoint = "https://sicredihom.service-now.com/api/now/table/u_z_category?sysparm_limit=1";
                snowRequest = buildSnowRequest(endPoint,"GET",requestBody);
                break;
            case 2:
                System.out.println("POST Selected");
                System.out.println("");
                requestBody = "{\"u_category\":\"Categoria.Exemplo.Api\",\"u_persid\":\"pcat:123\"}";
                endPoint = "https://sicredihom.service-now.com/api/now/table/u_z_category";
                snowRequest = buildSnowRequest(endPoint,"POST",requestBody);
                break;
            case 3:
                System.out.println("PUT Selected");
                requestBody = "{\"u_category\":\"Categoria Alterada\"}";
                endPoint = "https://sicredihom.service-now.com/api/now/table/u_z_category/b901d5981bf5bc901cc0a750f54bcbf0";
                snowRequest = buildSnowRequest(endPoint,"PUT",requestBody);
                break;
            case 4:
                System.out.println("DELETE Selected");
                endPoint = "https://sicredihom.service-now.com/api/now/table/u_z_category/b901d5981bf5bc901cc0a750f54bcbf0";
                snowRequest = buildSnowRequest(endPoint,"DELETE","");
                break;
            default:
                System.out.println("Invalid Operation");
                break;
        }

        exectuteSnowRequest(snowRequest);
    }

    private static HttpRequest buildSnowRequest(String endPoint, String method, String requestBody){
        URI snowEndPoint = URI.create(endPoint);
        HttpRequest snowRequest = null;

        snowRequest = HttpRequest.newBuilder()
            .uri(snowEndPoint)
            .method(method, BodyPublishers.ofString(requestBody))
            .setHeader("Accept", "application/json")
            .build();

        return snowRequest;
    }

    private static void exectuteSnowRequest(HttpRequest snowRequest){
        try {
            HttpResponse<String> response = httpClient.send(snowRequest, BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void writeMenu(){
        System.out.println( "SERVICE NOW API Explorer Exaple ========================" );
        System.out.println("");
        System.out.println("Type the desired option:");
        System.out.println("");
        System.out.println("1 - Select Objetct | GET Operation");
        System.out.println("2 - Create Objetct | POST Operation");
        System.out.println("3 - Update Objetct | PUT Operation");
        System.out.println("4 - Delete Objetct | DELETE Operation");
        System.out.println("");
        System.out.print("Your selection:");
    }
}
