package com.community.infra.aws;

import com.community.service.GraduationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class S3Controller {

    private final S3Repository s3Repository;
    private final S3Service s3Service;

    @ResponseBody
    @RequestMapping(value = "/s3/image/delete", method = RequestMethod.POST)
    public ResponseEntity graduationDeleteImage(@RequestParam(value = "imageName") String imageName) {
        S3 s3 = s3Repository.findByImageName(imageName);

        s3Service.deleteImage(s3);

        if(s3 != null) ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }
}
