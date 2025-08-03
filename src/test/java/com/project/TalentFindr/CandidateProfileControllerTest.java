package com.project.TalentFindr;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content; // ✅ Correct import
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import com.project.TalentFindr.service.JobPostActivityService;
import com.project.TalentFindr.service.UsersService;
import com.project.TalentFindr.util.FileDownloadUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest // ✅ Needed to load controller for MockMvc
public class CandidateProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileDownloadUtil fileDownloadUtil;

    @MockBean
    private UsersService usersService;

    @MockBean
    private  JobPostActivityService jobPostActivityService;



    @Test
    public void testDownloadResumeSuccess() throws Exception {
        Resource mockResource = new ByteArrayResource("test content".getBytes()) {
            @Override public String getFilename() { return "resume.pdf"; }
            @Override public boolean exists() { return true; }
        };

        when(fileDownloadUtil.getFileAsResource(anyString(), eq("resume.pdf")))
                .thenReturn(mockResource);

        mockMvc.perform(get("/files/downloadResume")
                .param("fileName", "resume.pdf")
                .param("userID", "123"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\""))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM)); // ✅ No cast
    }

    @Test
    public void testDownloadResumeFileNotFound() throws Exception {
        when(fileDownloadUtil.getFileAsResource(anyString(), anyString()))
                .thenReturn(null);

        mockMvc.perform(get("/files/downloadResume")
                .param("fileName", "nonexistent.pdf")
                .param("userID", "123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resume not found")); // ✅ No cast
    }

    @Test
    public void testDownloadResumeThrowsIOException() throws Exception {
        when(fileDownloadUtil.getFileAsResource(anyString(), anyString()))
                .thenThrow(new IOException("Disk error"));

        mockMvc.perform(get("/files/downloadResume")
                .param("fileName", "resume.pdf")
                .param("userID", "123"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Resume could not be found or accessed.")); // ✅ No cast
    }
}
