package hmoa.hmoaserver.member.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.exception.ExceptionResponseDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.dto.*;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Api(tags="멤버")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final JwtService jwtService;
    private final MemberService memberService;

    @Value("${defalut.profile}")
    private String DEFALUT_PROFILE_URL;



    /**
     * 멤버 단건 조회
     */
    @ApiOperation(value = "멤버 단건 조회")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답",
                    response = MemberResponseDto.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "토큰이 없거나 잘못됐습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @GetMapping("/member")
    public ResponseEntity<MemberResponseDto> findOneMember(HttpServletRequest request, @RequestHeader("X-AUTH-TOKEN") String token) {
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        MemberResponseDto resultDto = new MemberResponseDto(findMember);
        if(findMember.getImgUrl()==null){
            resultDto.setImgUrl(DEFALUT_PROFILE_URL);
        }
        return ResponseEntity.ok(resultDto);

    }

    /**
     * 회원 가입시 정보 수정
     */
    @ApiOperation(value = "회원 가입")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답",
                    response = MemberResponseDto.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "토큰이 없거나 잘못됐습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @PatchMapping("/member/join")
    public ResponseEntity<MemberResponseDto> joinMember(@RequestBody JoinUpdateRequestDto request, @RequestHeader("X-AUTH-TOKEN") String token){
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        memberService.joinMember(findMember,request.getAge(),request.getSex(),request.getNickname());
        MemberResponseDto reslutDto = new MemberResponseDto(findMember);
        return ResponseEntity.ok(reslutDto);
    }

    /**
     * 닉네임 변경
     */
    @ApiOperation(value = "닉네임 변경")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답"
            ),
            @ApiResponse(
                    code = 401,
                    message = "토큰이 없거나 잘못됐습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "이미 존재하는 닉네임입니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @PatchMapping("/member/nickname")
    public ResponseEntity<ResultDto<Object>> updateNickname(@RequestBody NicknameRequestDto request, @RequestHeader("X-AUTH-TOKEN") String token){
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        memberService.updateNickname(findMember, request.getNickname());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .resultCode("NICKNAME_UPDATE")
                        .message("닉네임 변경 완료")
                        .build());
    }

    /**
     * 닉네임 중복 검사
     */
    @ApiOperation(value = "닉네임 중복 검사")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답"
            ),
            @ApiResponse(
                    code = 401,
                    message = "인증 실패",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "이미 존재하는 닉네임입니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @PostMapping("/member/existsnickname")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestBody NicknameRequestDto request){
        return ResponseEntity.ok(memberService.isExistingNickname(request.getNickname()));
    }

    /**
     * 나이 업데이트
     */
    @ApiOperation(value = "나이 업데이트")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답"
            ),
            @ApiResponse(
                    code = 401,
                    message = "인증 실패",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "이미 존재하는 닉네임입니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @PatchMapping("/member/age")
    public ResponseEntity<ResultDto<Object>> updateAge(@RequestBody AgeRequestDto request, @RequestHeader("X-AUTH-TOKEN") String token){
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        memberService.updateAge(findMember, request.getAge());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .resultCode("AGE_UPDATE")
                        .message("나이 업데이트 완료")
                        .build());
    }

    /**
     * 나이 업데이트
     */
    @ApiOperation(value = "성별 업데이트")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답"
            ),
            @ApiResponse(
                    code = 401,
                    message = "인증 실패",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "이미 존재하는 닉네임입니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @PatchMapping("/member/sex")
    public ResponseEntity<ResultDto<Object>> updateSex(@RequestBody SexRequestDto request, @RequestHeader("X-AUTH-TOKEN") String token){
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        memberService.updateSex(findMember, request.getSex());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .resultCode("AGE_UPDATE")
                        .message("나이 업데이트 완료")
                        .build());
    }
    @ApiOperation(value = "내가 쓴 댓글")
    @GetMapping("/member/comments")
    public ResponseEntity<List<PerfumeCommentResponseDto>> findMyComments(@RequestHeader("X-AUTH-TOKEN") String token){
        List<PerfumeComment> comments= memberService.findByComment(token);
        List<PerfumeCommentResponseDto> result = new ArrayList<>();
        for (PerfumeComment pc : comments){
            log.info("{}",pc.getId());
            PerfumeCommentResponseDto dto = new PerfumeCommentResponseDto(pc);
            result.add(dto);
        }
        return ResponseEntity.ok(result);
    }
}