namespace java es.udc.ws.app.thrift

struct ThriftEventDto{
    1: i64 eventId
    2: string name
    3: string description
    4: string startDate
    5: double duration
    6: bool cancelation
    7: i32 numberAssistance
    8: i32 numberTotalResponses
}

struct ThriftResponseDto{
    1: i64 responseId
    2: i64 eventId
    3: string userEmail
    4: bool assistance
}

exception ThriftInputValidationException{
    1: string message
}

exception ThriftInstanceNotFoundException{
    1: string instanceId
    2: string instanceType
}

exception ThriftEventAlreadyCanceled{
    1: string message
}

exception ThriftExistAnsweredForEvent{
    1: string message
}

exception ThriftDateResponseExpiration{
    1: string message
}

service ThriftEventService{
    ThriftEventDto registerEvent(1: ThriftEventDto eventDto) throws (1: ThriftInputValidationException e)

    list<ThriftEventDto> findEventsByKeyword(1: optional string keywords, 2: string endDate) throws (1: ThriftInputValidationException e)

    ThriftEventDto findEvent(1: i64 eventId) throws (1: ThriftInstanceNotFoundException e)

    ThriftResponseDto answerEvent(1: i64 eventId, 2: string userEmail, 3: bool assistance) throws (1: ThriftInstanceNotFoundException e, 2: ThriftInputValidationException ee,
                                    3: ThriftEventAlreadyCanceled eee, 4: ThriftExistAnsweredForEvent eeee, 5: ThriftDateResponseExpiration eeeee)
}
