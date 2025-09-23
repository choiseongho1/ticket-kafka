package com.study.ticket.domain.screening.controller;

import com.study.ticket.domain.screening.dto.*;
import com.study.ticket.domain.screening.domain.entity.Screening;
import com.study.ticket.domain.screening.service.ScreeningService;
import com.study.ticket.global.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 상영 컨트롤러
 */
@RestController
@RequestMapping("/api/screenings")
@RequiredArgsConstructor
@Slf4j
public class ScreeningController {

    // 성공 응답 코드
    private static final String SCREENING_CREATE_SUCCESS = "SCREENING_CREATE_SUCCESS";
    private static final String SCREENING_GET_SUCCESS = "SCREENING_GET_SUCCESS";
    private static final String SCREENING_LIST_SUCCESS = "SCREENING_LIST_SUCCESS";
    private static final String SCREENING_UPDATE_SUCCESS = "SCREENING_UPDATE_SUCCESS";
    private static final String SCREENING_DELETE_SUCCESS = "SCREENING_DELETE_SUCCESS";
    
    // 실패 응답 코드
    private static final String SCREENING_CREATE_FAILED = "SCREENING_CREATE_FAILED";
    private static final String SCREENING_GET_FAILED = "SCREENING_GET_FAILED";
    private static final String SCREENING_LIST_FAILED = "SCREENING_LIST_FAILED";
    private static final String SCREENING_UPDATE_FAILED = "SCREENING_UPDATE_FAILED";
    private static final String SCREENING_DELETE_FAILED = "SCREENING_DELETE_FAILED";

    private final ScreeningService screeningService;

    /**
     * 상영을 생성합니다.
     * @param request 상영 생성 요청
     * @return 생성된 상영
     */
    @PostMapping
    public ResponseEntity<ResponseDto<ScreeningResponse>> createScreening(@RequestBody ScreeningSaveDto screeningSaveDto) {
        try {

            screeningService.createScreening(screeningSaveDto);
            

            ResponseDto<ScreeningResponse> response = ResponseDto.<ScreeningResponse>builder()
                .responseCode(SCREENING_CREATE_SUCCESS)
                .responseMessage("상영이 성공적으로 생성되었습니다.")
                .build();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("상영 생성 실패: {}", e.getMessage(), e);
            
            ResponseDto<ScreeningResponse> errorResponse = ResponseDto.<ScreeningResponse>builder()
                .responseCode(SCREENING_CREATE_FAILED)
                .responseMessage("상영 생성에 실패했습니다: " + e.getMessage())
                .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 상영을 조회합니다.
     * @param screeningId 상영 ID
     * @return 상영
     */
    @GetMapping("/{screeningId}")
    public ResponseEntity<ResponseDto<ScreeningDetailDto>> findScreeningDetail(@PathVariable Long screeningId) {
        try {
            log.info("상영 조회 요청: {}", screeningId);
            
            ScreeningDetailDto detail = screeningService.findScreeningDetail(screeningId);

            ResponseDto<ScreeningDetailDto> response = ResponseDto.<ScreeningDetailDto>builder()
                .responseCode(SCREENING_GET_SUCCESS)
                .responseMessage("상영 조회에 성공했습니다.")
                .data(detail)
                .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("상영 조회 실패: {}", e.getMessage(), e);
            
            ResponseDto<ScreeningDetailDto> errorResponse = ResponseDto.<ScreeningDetailDto>builder()
                .responseCode(SCREENING_GET_FAILED)
                .responseMessage("상영 조회에 실패했습니다: " + e.getMessage())
                .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    /**
     * 상영 목록을 조회합니다.
     * @param ScreeningCondDto screeningCondDto
     * @param Pageable pageable
     * @return 상영
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<Page<ScreeningListDto>>> findScreeningListWithPaging(@ModelAttribute ScreeningCondDto screeningCondDto, @PageableDefault Pageable pageable) {
        try {

            Page<ScreeningListDto> list = screeningService.findScreeningListWithPaging(screeningCondDto, pageable);

            ResponseDto<Page<ScreeningListDto>> response = ResponseDto.<Page<ScreeningListDto>>builder()
                .responseCode(SCREENING_GET_SUCCESS)
                .responseMessage("상영 조회에 성공했습니다.")
                .data(list)
                .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("상영 조회 실패: {}", e.getMessage(), e);

            ResponseDto<Page<ScreeningListDto>> errorResponse = ResponseDto.<Page<ScreeningListDto>>builder()
                .responseCode(SCREENING_GET_FAILED)
                .responseMessage("상영 조회에 실패했습니다: " + e.getMessage())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    /**
     * 상영을 업데이트합니다.
     * @param screeningId 상영 ID
     * @param ScreeningUpdateDto screeningUpdateDto
     * @return 업데이트된 상영
     */
    @PutMapping("/{screeningId}")
    public ResponseEntity<ResponseDto<ScreeningResponse>> updateScreening(
            @PathVariable Long screeningId,
            @RequestBody ScreeningUpdateDto screeningUpdateDto
    ) {
        try {
            screeningService.updateScreening(screeningId,screeningUpdateDto);

            ResponseDto<ScreeningResponse> response = ResponseDto.<ScreeningResponse>builder()
                .responseCode(SCREENING_UPDATE_SUCCESS)
                .responseMessage("상영 업데이트에 성공했습니다.")
                .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("상영 업데이트 실패: {}", e.getMessage(), e);

            ResponseDto<ScreeningResponse> errorResponse = ResponseDto.<ScreeningResponse>builder()
                .responseCode(SCREENING_UPDATE_FAILED)
                .responseMessage("상영 업데이트에 실패했습니다: " + e.getMessage())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 상영을 삭제합니다.
     * @param screeningId 상영 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{screeningId}")
    public ResponseEntity<ResponseDto<Void>> deleteScreening(@PathVariable Long screeningId) {
        try {
            log.info("상영 삭제 요청: {}", screeningId);

            screeningService.deleteScreening(screeningId);

            ResponseDto<Void> response = ResponseDto.<Void>builder()
                .responseCode(SCREENING_DELETE_SUCCESS)
                .responseMessage("상영 삭제에 성공했습니다.")
                .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("상영 삭제 실패: {}", e.getMessage(), e);

            ResponseDto<Void> errorResponse = ResponseDto.<Void>builder()
                .responseCode(SCREENING_DELETE_FAILED)
                .responseMessage("상영 삭제에 실패했습니다: " + e.getMessage())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
