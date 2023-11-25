package xyz.interfacesejong.interfaceapi.domain.subscription.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.subscription.dto.SubscriptionRequest;
import xyz.interfacesejong.interfaceapi.domain.subscription.dto.SubscriptionResponse;
import xyz.interfacesejong.interfaceapi.domain.subscription.service.SubscriptionService;
import xyz.interfacesejong.interfaceapi.global.aop.Timer;
import xyz.interfacesejong.interfaceapi.global.exception.dto.BaseExceptionResponse;

@RestController
@RequestMapping("api/subscription")
@RequiredArgsConstructor
@Tag(name = "Subscription", description = "Topic 구독 관리 api")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("{userId}/fcm-token")
    @Timer
    @Operation(summary = "FCM Token 변경",
            description = "해당 id를 가지는 유저의 FCM Token을 새로 변경한다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "token 변경 성공"),
                    @ApiResponse(responseCode = "404", description = "존재 하지 않는 userId",
                            content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
            })
    public ResponseEntity<SubscriptionResponse> updateFcmToken(@PathVariable Long userId,
                                                               @RequestBody SubscriptionRequest request){
        SubscriptionResponse response = subscriptionService.updateFcmToken(userId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("{userId}/notice")
    @Timer
    @Operation(summary = "Notice topic 구독 상태 변경",
            description = "해당 id를 가지는 유저의 Notice 구독 정보를 변경한다.\n\n" +
                    "인자는 true가 구독을 하는 것으로 한다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "구독 상태 변경 성공"),
                    @ApiResponse(responseCode = "404", description = "존재 하지 않는 userId,\n\nnotice 항목이 null",
                            content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
            })
    public ResponseEntity<SubscriptionResponse> updateNoticeSubscriptionStatus(@PathVariable Long userId,
                                                                               @RequestBody SubscriptionRequest request){
        SubscriptionResponse response = subscriptionService.updateNoticeStatus(userId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("{userId}/schedule")
    @Timer
    @Operation(summary = "Notice topic 구독 상태 변경",
            description = "해당 id를 가지는 유저의 Schedule 구독 정보를 변경한다.\n\n" +
                    "인자는 true가 구독을 하는 것으로 한다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "구독 상태 변경 성공"),
                    @ApiResponse(responseCode = "404", description = "존재 하지 않는 userId,\n\nschedule 항목이 null",
                            content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
            })
    public ResponseEntity<SubscriptionResponse> updateScheduleSubscriptionStatus(@PathVariable Long userId,
                                                                               @RequestBody SubscriptionRequest request){
        SubscriptionResponse response = subscriptionService.updateScheduleStatus(userId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("{userId}/vote")
    @Timer
    @Operation(summary = "Vote topic 구독 상태 변경",
            description = "해당 id를 가지는 유저의 Notice 구독 정보를 변경한다.\n\n" +
                    "인자는 true가 구독을 하는 것으로 한다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "구독 상태 변경 성공"),
                    @ApiResponse(responseCode = "404", description = "존재 하지 않는 userId,\n\nvote 항목이 null",
                            content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
            })
    public ResponseEntity<SubscriptionResponse> updateVoteSubscriptionStatus(@PathVariable Long userId,
                                                                               @RequestBody SubscriptionRequest request){
        SubscriptionResponse response = subscriptionService.updateVoteStatus(userId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{userId}/Topic")
    @Timer
    @Operation(summary = "Topic 구독 상태 반환",
            description = "해당 id를 가지는 유저의 구독중인 Topic 정보를 반환한다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "topic 정보 반환 성공"),
                    @ApiResponse(responseCode = "404", description = "존재 하지 않는 userId",
                            content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
            })
    private ResponseEntity<SubscriptionResponse> getSubscriptionTopic(@PathVariable Long userId){
        SubscriptionResponse response = subscriptionService.getSubscriptionByUserId(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("{userId}/badge")
    @Timer
    @Operation(summary = "Vote topic 구독 상태 변경",
            description = "해당 id를 가지는 유저의 badge count를 0으로 변경한다",
            responses = {
                    @ApiResponse(responseCode = "204", description = "badge 0으로 초기화"),
                    @ApiResponse(responseCode = "404", description = "존재 하지 않는 userId",
                            content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
            })
    public ResponseEntity<Void> resetBadgeCount(@PathVariable Long userId){
        subscriptionService.resetBadgeCount(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




    
    
}
