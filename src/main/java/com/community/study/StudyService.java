package com.community.study;

import com.community.account.entity.Account;
import com.community.alarm.study.StudyCreatedPublish;
import com.community.board.entity.Reply;
import com.community.board.repository.ReplyRepository;
import com.community.board.service.BoardService;
import com.community.study.entity.Meetings;
import com.community.study.entity.Study;
import com.community.study.form.MeetingsForm;
import com.community.study.form.StudyCalendarForm;
import com.community.study.form.StudyDescriptionForm;
import com.community.study.repository.MeetingsRepository;
import com.community.study.repository.StudyRepository;
import com.community.tag.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudyService {

    private final StudyRepository studyRepository;
    private final MeetingsRepository meetingsRepository;
    private final ReplyRepository replyRepository;

    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Study createNewStudy(Study study, Account account) {
        Study newStudy = studyRepository.save(study);
        newStudy.addManager(account);

        return newStudy;
    }

    public Study getPath(String path) {

        Study byPath = this.studyRepository.findByPath(path);
        if (byPath == null) {
            throw new AccessDeniedException("유효하지 않는 페이지입니다.");
        }
        return byPath;
    }

    public Study getStudyUpdate(Account account, String path) {
        Study byPath = this.getPath(path);
        if (!account.isManager(byPath) && !account.isMemeber(byPath)) {
            throw new IllegalArgumentException("해당하는 스터디가 없습니다.");
        }
        return byPath;
    }

    public void getStudyImage(Study study, String image) {
        study.setImage(image);
    }

    public Study getStudyToUpdateStatus(Account account, String path) {
        Study accountWithManagersByPath = studyRepository.findAccountWithManagersByPath(path);
        checkExistStudy(path, accountWithManagersByPath);
        checkManager(account, accountWithManagersByPath);

        return accountWithManagersByPath;
    }

    public Study getStudyToUpdateStatusByMember(String path) {
        Study accountWithManagersByPath = studyRepository.findAccountWithManagersByPath(path);
        checkExistStudy(path, accountWithManagersByPath);

        return accountWithManagersByPath;
    }

    public Study getStudyUpdateTag(Account account, String path) {
        Study accountWithTagsByPath = studyRepository.findAccountWithTagsByPath(path);
        Study byPath = studyRepository.findByPath(path);

        applicationEventPublisher.publishEvent(new StudyCreatedPublish(byPath));

        checkExistStudy(path, accountWithTagsByPath);
        checkManager(account, accountWithTagsByPath);

        return accountWithTagsByPath;
    }


    public void addTag(Study studyUpdateTag, Tag byTitle) {
        studyUpdateTag.getTags().add(byTitle);
    }
    public void removeTag(Study studyUpdateTag, Tag byTitle) {
        studyUpdateTag.getTags().remove(byTitle);
    }


    private void checkManager(Account account, Study accountWithTagsByPath) {
        if (!account.isManager(accountWithTagsByPath)) throw new AccessDeniedException("해당 기능을 사용할 권한이 없습니다.");

    }

    private void checkExistStudy(String path, Study accountWithTagsByPath) {
        if (accountWithTagsByPath == null) throw new IllegalArgumentException(path + "해당 하는 스터디가 없습니다.");
    }

    public void recruitClose(Study studyUpdate) {
        studyUpdate.recruitClose();
    }

    public void updateStudyTitle(Study studyToUpdateStatus, String newTitle) {
        studyToUpdateStatus.setTitle(newTitle);
    }

    public boolean isValidTitle(String newTitle) {
        return newTitle.length() <= 50;
    }

    public void addMember(Study studyWithMembersByPath, Account account) {
        studyWithMembersByPath.addMember(account);
    }

    public void removeMember(Study studyWithMembersByPath, Account account) {
        studyWithMembersByPath.removeMember(account);
    }

    public boolean isValidPath(String newPath) {
        if(!newPath.matches("^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{2,20}$")){
            return false;
        }

        return !studyRepository.existsByPath(newPath);
    }

    public void updateStudyPath(Study studyToUpdateStatus, String newPath) {
        studyToUpdateStatus.setPath(newPath);
    }

    public void remove(Study study) {
        studyRepository.delete(study);
    }

    public void updateStudyCalendar(Study study, StudyCalendarForm studyCalendarForm) {
        modelMapper.map(studyCalendarForm, study);
    }

    public void updateStudyDescription(Study study, StudyDescriptionForm studyDescriptionForm) {
        modelMapper.map(studyDescriptionForm, study);
    }

    public Study getStudyManager(String path) {

        return studyRepository.findAccountWithManagersByPath(path);
    }

    public Meetings createNewMeeting(Meetings meetings, Study study, Account account) {
        meetings.setWriter(account);
        meetings.setUploadTime(LocalDateTime.now());
        meetings.setStudy(study);

        return meetingsRepository.save(meetings);

    }

    public String meetingDateTime(LocalDateTime localDateTime){
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if (diffTime < BoardService.SEC) {
            // sec
            msg = diffTime + "초 전";
        } else if ((diffTime /= BoardService.SEC) < BoardService.MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= BoardService.MIN) < BoardService.HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= BoardService.HOUR) < BoardService.DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= BoardService.DAY) < BoardService.MONTH) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }

    public List<Reply> replyList(Meetings meetings) {

        return replyRepository.findAllByMeetingsOrderByUploadTimeDesc(meetings);
    }

    /*모임 주제 쉼표 구분*/
    public List<String> getMeetingTagsList(Meetings meetings) {

        String[] meetingTagsArray = meetings.getMeetingMethod().split(",");

        List<String> meetingTagsList = new ArrayList<>();
        Collections.addAll(meetingTagsList, meetingTagsArray);

        return meetingTagsList;
    }

    public void updateMeeting(long meetingId, MeetingsForm meetingsForm) {
        Meetings meetings = meetingsRepository.findByMeetingsId(meetingId);

        meetings.setMeetingTitle(meetingsForm.getMeetingTitle());
        meetings.setMeetingPlaces(meetingsForm.getMeetingPlaces());
        meetings.setMeetingDescription(meetingsForm.getMeetingDescription());

        if(meetingsForm.getMeetingMethod() != null) {
            meetings.setMeetingMethod(meetingsForm.getMeetingMethod());
        }

        meetingsRepository.save(meetings);
    }

}
