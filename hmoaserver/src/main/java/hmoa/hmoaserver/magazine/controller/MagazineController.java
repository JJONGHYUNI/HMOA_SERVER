package hmoa.hmoaserver.magazine.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.magazine.domain.Magazine;
import hmoa.hmoaserver.magazine.dto.MagazineSaveRequestDto;
import hmoa.hmoaserver.magazine.service.MagazineService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.photo.domain.MagazinePhoto;
import hmoa.hmoaserver.photo.service.MagazinePhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "매거진")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/magazine")
public class MagazineController {
    private final MagazineService magazineService;
    private final MagazinePhotoService magazinePhotoService;

    @ApiOperation("매거진 저장")
    @PostMapping("/save")
    public ResponseEntity<ResultDto> saveMagazine(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody MagazineSaveRequestDto dto) {
        Magazine magazine = magazineService.save(dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("매거진 사진 저장")
    @PostMapping(value = "/saveImage/{magazineId}", consumes = "multipart/form-data")
    public ResponseEntity<ResultDto> saveMagazineImage(@PathVariable Long magazineId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestPart(value = "image") List<MultipartFile> files) {
        Magazine magazine = magazineService.findById(magazineId);

        List<MagazinePhoto> photos = magazinePhotoService.saveMagazinePhoto(magazine, files);
        magazineService.saveImages(magazine, photos);

        return ResponseEntity.ok(ResultDto.builder().build());
    }
}