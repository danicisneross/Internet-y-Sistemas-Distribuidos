package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientAppService;
import es.udc.ws.app.client.service.ClientAppServiceFactory;
import es.udc.ws.app.client.service.dto.ClientEventDto;
import es.udc.ws.app.client.service.dto.ClientResponseDto;
import es.udc.ws.app.client.service.exceptions.ClientDateResponseExpirationException;
import es.udc.ws.app.client.service.exceptions.ClientEventAlreadyCanceledException;
import es.udc.ws.app.client.service.exceptions.ClientExistAnsweredForEventException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class AppServiceClient {
    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientAppService clientAppService = ClientAppServiceFactory.getService();

        if("-registerEvent".equalsIgnoreCase(args[0])){
            validateArgs(args, 5, 5, new int[] {});

            // [register event] EventServiceClient -registerEvent <name> <description> <star_date> <end_date>

            try{
                Long eventId = clientAppService.registerEvent(new ClientEventDto(args[1], args[2], LocalDateTime.parse(args[3]), LocalDateTime.parse(args[4]), null, false, 0, 0));
                System.out.println("Event " + eventId + " created sucessfully");
            }catch (NumberFormatException | InputValidationException ex){
                ex.printStackTrace(System.err);
            }catch (Exception ex){
                ex.printStackTrace(System.err);
            }
        } else if("-findEvents".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, 3, new int[] {});

            // [find events]  EventServiceClient -findEvents <untilDate> [<keyword>]

            try {
                if(args.length==3){
                    List<ClientEventDto> events = clientAppService.findEventsByKeyword(args[2], LocalDateTime.now(), LocalDateTime.parse(args[1]));
                    System.out.println("Found " + events.size() + " event(s) with keywords '" + args[2] + "' and endDate= " + LocalDateTime.parse(args[1]));
                    for (int i = 0; i < events.size(); i++) {
                        ClientEventDto eventDto = events.get(i);
                        System.out.println("Id: " + eventDto.getEventId() +
                                ", Name: " + eventDto.getName() +
                                ", Description: " + eventDto.getDescription() +
                                ", StartDate: " + eventDto.getStartDate() +
                                ", Cancelation: " + eventDto.getCancelation() +
                                ", NumberAssistance: " + eventDto.getNumberAssistance() +
                                ", NumberTotalResponses: " + eventDto.getNumberTotalResponses()) ;
                    }
                }
                else{
                    List<ClientEventDto> events = clientAppService.findEventsByKeyword(null, LocalDateTime.now(), LocalDateTime.parse(args[1]));
                    System.out.println("Found " + events.size() + " event(s) with endDate= " + LocalDateTime.parse(args[1]));
                    for (int i = 0; i < events.size(); i++) {
                        ClientEventDto eventDto = events.get(i);
                        System.out.println("Id: " + eventDto.getEventId() +
                                ", Name: " + eventDto.getName() +
                                ", Description: " + eventDto.getDescription() +
                                ", StartDate: " + eventDto.getStartDate() +
                                ", Cancelation: " + eventDto.getCancelation() +
                                ", NumberAssistance: " + eventDto.getNumberAssistance() +
                                ", NumberTotalResponses: " + eventDto.getNumberTotalResponses()) ;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-findEvent".equalsIgnoreCase(args[0])){
            validateArgs(args, 2, 2, new int[] {1});

            // [find event]     EventServiceClient -findEvent <eventId>

            try{
                ClientEventDto event = clientAppService.findEvent(Long.parseLong(args[1]));
                System.out.println("Id: " + event.getEventId() +
                        ", Name: " + event.getName() +
                        ", Description: " + event.getDescription() +
                        ", StartDate: " + event.getStartDate() +
                        ", EndDate: " + event.getEndDate() +
                        ", Cancelation: " + event.getCancelation() +
                        ", NUmberAssistance: " + event.getNumberAssistance() +
                        ", NumberTotalResponses: " + event.getNumberTotalResponses());
            } catch (InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-answerEvents".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, 4, new int[] {1});

            // [respond]  EventServiceClient -respond <eventId> <userEmail> <response>

            Long responseId;
            try {
                responseId = clientAppService.answerEvents(Long.parseLong(args[1]), args[2], Boolean.parseBoolean(args[3]));

                System.out.println("Event " + args[1] + " responded by " + args[2] + " with response number " + responseId);

            } catch (ClientEventAlreadyCanceledException | InstanceNotFoundException |
                     ClientExistAnsweredForEventException |
                     InputValidationException | ClientDateResponseExpirationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-cancel".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, 2, new int[] {1});

            // [cancel]  EventServiceClient -cancel <eventId>

            try {
                clientAppService.cancelEvent(Long.parseLong(args[1]));

                System.out.println("EventId =  " + args[1] + " canceled");

            } catch (NumberFormatException | ClientEventAlreadyCanceledException |
                     InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }else if ("-findAnswer".equalsIgnoreCase(args[0])){
            validateArgs(args, 3, 3, new int[] {});

            // [find responses] EventServiceClient -findResponses <userEmail> <onlyAffirmative>

            try {
                List<ClientResponseDto> responses = clientAppService.findAnswerEvent(args[1], Boolean.parseBoolean(args[2]));
                System.out.println("Found " + responses.size() + " response(s) with userEmail '" + args[1] + "'");

                for (ClientResponseDto responseDto : responses) {
                    System.out.println("ResponseId: " + responseDto.getResponseId() +
                            ", EventId: " + responseDto.getEventId() +
                            ", UserEmail: " + responseDto.getUserEmail() +
                            ", Assitance: " + responseDto.isAssistance());
                }
            }catch (Exception ex){
                ex.printStackTrace(System.err);
            }
        }
    }

    public static void validateArgs(String[] args, int minExpectedArgs, int maxExpectedArgs, int[] numericArguments) {
        if(minExpectedArgs > args.length || maxExpectedArgs < args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [register event] AppServiceClient -registerEvent <name> <description> <star_date> <end_date>\n" +
                "    [find events]    AppServiceClient -findEvents <untilDate> [<keyword>]\n" +
                "    [find event]     AppServiceClient -findEvent <eventId>\n" +
                "    [respond]        AppServiceClient -answerEvents <userEmail> <eventId> <response>\n" +
                "    [cancel]         AppServiceClient -cancel <eventId>\n" +
                "    [find responses] AppServiceClient -findResponses <userEmail> <onlyAffirmative>\n");
    }
}