package com.finalproject.safepickup.Service;

import com.finalproject.safepickup.Api.ApiException;
import com.finalproject.safepickup.DTOout.ExitLogResponseDTO;
import com.finalproject.safepickup.Model.ExitLog;
import com.finalproject.safepickup.Repository.ExitLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExitLogService {
    private final ExitLogRepository exitLogRepository;

    // 1- Get all exit logs
    public List<ExitLogResponseDTO> findAll() {
        List<ExitLog> exitLogs = exitLogRepository.findAll();
        if (exitLogs == null) {
            throw new ApiException("Exit logs not found");
        }
        return exitLogs.stream()
                .map(ExitLogResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 2- Get exit logs by student
    public List<ExitLogResponseDTO> findByStudentId(Integer studentId) {
        List<ExitLog> exitLogs = exitLogRepository.findByStudentId(studentId);
        if (exitLogs == null) {
            throw new ApiException("Exit logs for student not found");
        }
        return exitLogs.stream()
                .map(ExitLogResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 3- Get exit logs by parent (shows all their children's logs)
    public List<ExitLogResponseDTO> findByParentId(Integer parentId) {
        List<ExitLog> exitLogs = exitLogRepository.findByParentId(parentId);
        if (exitLogs == null) {
            throw new ApiException("Exit logs for parent not found");
        }
        return exitLogs.stream()
                .map(ExitLogResponseDTO::new)
                .collect(Collectors.toList());
    }


}
