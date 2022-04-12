package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.board.Reply;
import com.community.domain.board.ReplyRepository;
import com.community.domain.study.Meetings;
import com.community.domain.study.Study;
import com.community.domain.study.StudyRepository;
import com.community.web.dto.MeetingsForm;
import com.community.web.dto.StudyCalendarForm;
import com.community.web.dto.StudyDescriptionForm;
import com.community.domain.study.MeetingsRepository;
import com.community.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

        return this.getPath(path);
    }

    public void getStudyImage(Study study, String studyBannerKey, String studyBannerPath) {
        study.setImage(studyBannerPath);
        study.setImageKey(studyBannerKey);

        studyRepository.save(study);
    }

    public Study getStudyToUpdateStatus(Account account, String path) {
        Study accountWithManagersByPath = studyRepository.findAccountWithManagersByPath(path);
        checkExistStudy(path, accountWithManagersByPath);
        checkManager(account, accountWithManagersByPath);

        return accountWithManagersByPath;
    }

    public Study getStudyUpdateTag(Account account, String path) {
        Study accountWithTagsByPath = studyRepository.findAccountWithTagsByPath(path);
        Study byPath = studyRepository.findByPath(path);

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

    public Meetings createNewMeeting(Meetings meetings, Study study, Account account) {
        meetings.setWriter(account);
        meetings.setUploadTime(LocalDateTime.now());
        meetings.setStudy(study);

        return meetingsRepository.save(meetings);

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

    public boolean checkAlarmDateTime(Study studyUpdate) {

        if (studyUpdate.getRecentAlarmDateTime() == null) {
            return true;
        }
        return studyUpdate.getRecentAlarmDateTime().isBefore(LocalDateTime.now().minusHours(24));

    }

    public void blockMembers(Account account, Study study) {
        study.addBlockMembers(account);
        study.removeMember(account);
    }

    public void unBlockMembers(Account account, Study study) {
        study.getBlockMembers().remove(account);
    }

    public boolean checkBlockMembers(Study study, Account account) {
        boolean contains = study.getBlockMembers().contains(account);
        return !contains;
    }
}
