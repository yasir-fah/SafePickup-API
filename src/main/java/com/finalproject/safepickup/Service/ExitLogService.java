package com.finalproject.safepickup.Service;

import com.finalproject.safepickup.Api.ApiException;
import com.finalproject.safepickup.DTOout.ExitLogResponseDTO;
import com.finalproject.safepickup.Model.ExitLog;
import com.finalproject.safepickup.Model.Parent;
import com.finalproject.safepickup.Repository.ExitLogRepository;
import com.finalproject.safepickup.Repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExitLogService {
    private final ExitLogRepository exitLogRepository;
    private final ParentRepository parentRepository;

    // 1- Get all exit logs
    public List<ExitLogResponseDTO> findAll() {
        List<ExitLog> exitLogs = exitLogRepository.findAll();
        return exitLogs.stream()
                .map(ExitLogResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 2- Get exit logs by student
    public List<ExitLogResponseDTO> findByStudentId(Integer studentId) {
        List<ExitLog> exitLogs = exitLogRepository.findByStudentId(studentId);
        return exitLogs.stream()
                .map(ExitLogResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 3- Get exit logs by parent (shows all their children's logs)
    public List<ExitLogResponseDTO> findByParentId(Integer parentId) {
        List<ExitLog> exitLogs = exitLogRepository.findByParentId(parentId);
        return exitLogs.stream()
                .map(ExitLogResponseDTO::new)
                .collect(Collectors.toList());
    }

    /* 4- endpoint will be linked: parent-records and logs
     * service will return list of student logs of some parent (history)
     * */
    public List<ExitLogResponseDTO> studentLogForParent(Integer parentId) {
        Parent parent = parentRepository.findParentById(parentId);
        if(parent == null) {
            throw new ApiException("parent not found");
        }

        List<ExitLog> exitLogs = exitLogRepository.findByParentId(parentId);

        return exitLogs.stream()
                .map(ExitLogResponseDTO::new)
                .collect(Collectors.toList());
    }

    /* 5- endpoint will be linked: parent-records and logs
     * service will return list of all student logs (history)
     * */
    public List<ExitLogResponseDTO> findAllLogsForAdmin() {
        List<ExitLog> exitLogs = exitLogRepository.findAll();
        return  exitLogs.stream()
                .map(ExitLogResponseDTO::new)
                .collect(Collectors.toList());
    }


}
