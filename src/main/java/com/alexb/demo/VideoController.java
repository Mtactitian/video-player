package com.alexb.demo;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@RestController
public class VideoController {

    @GetMapping(value = "/v1")
    public void getEpisodeFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultipartFileSender.fromPath(Paths.get("/Users/alexb/Downloads/nav.mp4"))
                .with(request)
                .with(response)
                .serveResource();
    }

    @GetMapping(value = "/v2")
    public ResponseEntity get(@RequestHeader(value = "range", required = false) String rangeHeader) throws IOException {
        Path path = Paths.get("/Users/alexb/Downloads/nav.mp4");
        long length = Files.size(path);
        String contentType = "video/mp4";
        HttpStatus httpStatus = HttpStatus.OK;
        HttpRange range = HttpRange.createByteRange(0, length);

        if (rangeHeader != null) {
//            bytes=39092224-42437006
//            bytes=39092224-

            if (rangeHeader.matches("\\w*=\\d-")) {
                range = HttpRange.createByteRange(Long.parseLong(rangeHeader.replaceAll("bytes=", "").replaceAll("-", "")));
            } else {

                String[] ranges = rangeHeader.split("-");
                if (ranges.length > 1) {
                    range = HttpRange.createByteRange(Long.parseLong(ranges[0]), Long.parseLong(ranges[1]));
                } else {
                    range = HttpRange.createByteRange(Long.parseLong(ranges[0]), length);
                }
                httpStatus = HttpStatus.PARTIAL_CONTENT;
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", contentType);
        headers.add("Accept-Ranges", "bytes");
        headers.add("ETag", path.getFileName().toString());
        headers.setRange(Collections.singletonList(range));

        return new ResponseEntity<>(new InputStreamResource(Files.newInputStream(path)), headers, httpStatus);
    }


}
