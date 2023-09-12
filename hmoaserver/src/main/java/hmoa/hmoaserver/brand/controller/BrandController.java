package hmoa.hmoaserver.brand.controller;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.domain.BrandLikedMember;
import hmoa.hmoaserver.brand.dto.BrandDefaultResponseDto;
import hmoa.hmoaserver.brand.dto.BrandSaveRequestDto;
import hmoa.hmoaserver.brand.service.BrandLikedMemberService;
import hmoa.hmoaserver.brand.service.BrandService;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.dto.PerfumeDefaultResponseDto;
import hmoa.hmoaserver.perfume.dto.PerfumeDetailResponseDto;
import hmoa.hmoaserver.perfume.dto.PerfumeSimilarResponseDto;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import hmoa.hmoaserver.photo.service.BrandPhotoService;
import hmoa.hmoaserver.photo.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

import static hmoa.hmoaserver.exception.Code.DUPLICATE_LIKED;

@Api(tags = {"브랜드"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/brand")
public class BrandController {

    private final BrandService brandService;
    private final PhotoService photoService;
    private final BrandPhotoService brandPhotoService;
    private final JwtService jwtService;
    private final MemberService memberService;
    private final BrandLikedMemberService brandLikedMemberService;
    private final PerfumeService perfumeService;

    @ApiOperation(value = "브랜드 저장")
    @PostMapping(value = "/new")
    public ResponseEntity<ResultDto<Object>> saveBrand(HttpServletRequest request, @RequestParam(value="image",required = false) MultipartFile file, BrandSaveRequestDto requestDto) {

        Brand brand = brandService.save(requestDto);
        if(file!=null) {
            log.info("?");
            photoService.validateFileExistence(file);
            photoService.validateFileType(file);

            brandPhotoService.saveBrandPhotos(brand, file);
        }
        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation(value = "브랜드 단건 조회")
    @GetMapping("/{brandId}")
    public ResponseEntity<ResultDto<Object>> findOneBrand(@PathVariable Long brandId) {
        Brand brand = brandService.findById(brandId);

        BrandDefaultResponseDto responseDto = new BrandDefaultResponseDto(brand);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(responseDto)
                        .build());
    }

    @ApiOperation(value = "브랜드 공감하기")
    @PutMapping("/{brandId}/like")
    public ResponseEntity<ResultDto<Object>> saveBrandLikes(
            @PathVariable Long brandId, @RequestHeader("X-AUTH-TOKEN") String token
    ) {
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);

        Brand brand = brandService.findById(brandId);

        if (!brandLikedMemberService.isMemberLikedBrand(member, brand)) {
            brandLikedMemberService.save(member, brand);
            return ResponseEntity.status(200)
                    .body(ResultDto.builder()
                            .build()
                    );
        }
        throw new CustomException(null, DUPLICATE_LIKED);

    }

    @ApiOperation(value = "브랜드 공감 취소하기")
    @DeleteMapping("{brandId}/like")
    public ResponseEntity<ResultDto<Object>> deleteBrandLikes(
            @PathVariable Long brandId, @RequestHeader("X-AUTH-TOKEN") String token
    ) {
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);
        Brand brand = brandService.findById(brandId);

        BrandLikedMember brandLikedMember = brandLikedMemberService.findOneByBrandAndMember(brand, member);
        brandLikedMemberService.decrementLikedCountsOfBrand(brand);
        brandLikedMemberService.delete(brandLikedMember);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build()
                );
    }

    @ApiOperation(value = "브랜드별 향수 목록 조회(최신순)")
    @GetMapping("/perfumes/{brandId}/update")
    public ResponseEntity<ResultDto<Object>> findUpdatePerfumesByBrand(@PathVariable Long brandId, @RequestParam int pageNum) {

        brandService.findById(brandId);
        Page<Perfume> perfumes = perfumeService.findUpdatePerfumesByBrand(brandId, pageNum);

        List<PerfumeSimilarResponseDto> response = perfumes.stream()
                .map(perfume -> new PerfumeSimilarResponseDto(perfume)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "브랜드별 향수 목록 조회(문자열순)")
    @GetMapping("/perfumes/{brandId}")
    public ResponseEntity<ResultDto<Object>> findPerfumesByBrand(@PathVariable Long brandId, @RequestParam int pageNum) {

        brandService.findById(brandId);
        Page<Perfume> perfumes = perfumeService.findPerfumesByBrand(brandId, pageNum);

        List<PerfumeSimilarResponseDto> response = perfumes.stream()
                .map(perfume -> new PerfumeSimilarResponseDto(perfume)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "브랜드별 향수 목록 조회(좋아요순)")
    @GetMapping("/perfumes/{brandId}/top")
    public ResponseEntity<ResultDto<Object>> findTopPerfumesByBrand(@PathVariable Long brandId, @RequestParam int pageNum) {

        brandService.findById(brandId);
        Page<Perfume> perfumes = perfumeService.findTopPerfumesByBrand(brandId, pageNum);

        List<PerfumeSimilarResponseDto> response = perfumes.stream()
                .map(perfume -> new PerfumeSimilarResponseDto(perfume)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(response)
                        .build()
                );
    }

}
