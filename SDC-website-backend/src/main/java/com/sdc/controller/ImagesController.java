package com.sdc.controller;

import com.sdc.entity.Images;
import com.sdc.models.ImagesModel;
import com.sdc.services.ImagesService;
import com.sdc.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/images")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasRole('ADMIN')")
public class ImagesController {

    @Autowired
    private ImagesService imagesService;

    // ✅ Add image
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> uploadImage(@ModelAttribute ImagesModel model) {
        try {
            Images saved = imagesService.saveImage(model);
            return ResponseEntity.ok(new ApiResponse(true, "Image uploaded", convertToResponse(saved)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Upload failed", null));
        }
    }

    // ✅ Update image
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> updateImage(
            @PathVariable Integer id,
            @ModelAttribute ImagesModel model) {
        try {
            return imagesService.updateImage(id, model)
                    .map(updated -> ResponseEntity.ok(new ApiResponse(true, "Image updated", convertToResponse(updated))))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse(false, "Image not found", null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Update failed", null));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> getAllImages() {
        List<Images> images = imagesService.getAllImages();

        List<Map<String, Object>> response = images.stream().map(img -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", img.getId());
            map.put("title", img.getTitle());

            if (img.getImage() != null && img.getImage().length > 0) {
                map.put("imageBase64", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(img.getImage()));
            } else {
                map.put("imageBase64", null);
            }

            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "All images fetched", response));
    }

    // ✅ Delete image
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Integer id) {
        boolean deleted = imagesService.deleteImage(id);
        return ResponseEntity.ok(new ApiResponse(deleted,
                deleted ? "Image deleted successfully" : "Image not found",
                null));
    }

    // ✅ Convert entity to response map (with base64 image)
    private Map<String, Object> convertToResponse(Images image) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", image.getId());
        map.put("title", image.getTitle());

        if (image.getImage() != null && image.getImage().length > 0) {
            map.put("imageBase64", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(image.getImage()));
        } else {
            map.put("imageBase64", null);
        }

        return map;
    }
}
