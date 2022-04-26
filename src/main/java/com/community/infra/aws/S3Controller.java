package com.community.infra.aws;

import com.community.service.GraduationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class S3Controller {

    private final S3Repository s3Repository;
    private final S3Service s3Service;

    @ResponseBody
    @RequestMapping(value = "/s3/image/delete", method = RequestMethod.GET)
    public ResponseEntity graduationDeleteImage(@RequestParam(value = "imageId", required = false) int imageId) {
        log.info("s3 id 값 : {}", imageId);

        Optional<S3> byId = s3Repository.findById(Long.valueOf(imageId));
        S3 s3 = byId.get();

        s3Service.deleteImage(s3);

        return ResponseEntity.ok().build();
    }
}
