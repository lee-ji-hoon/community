package com.community.study;

import com.community.account.entity.Account;
import com.community.study.form.StudyDescriptionForm;
import com.community.tag.Tag;
import com.community.zone.Zone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudyService {

    private final StudyRepository repository;
    private final ModelMapper modelMapper;

    public Study createNewStudy(Study study, Account account) {
        Study newStudy = repository.save(study);
        newStudy.addManager(account);
        return newStudy;
    }

    public Study getPath(String path) {
        Study byPath = this.repository.findByPath(path);
        if (byPath == null) {
            throw new AccessDeniedException("유효하지 않는 페이지입니다.");
        }
        return byPath;
    }

    public Study getStudyUpdate(Account account, String path) {
        Study byPath = this.getPath(path);
        if (!account.isManager(byPath)) {
            throw new IllegalArgumentException("해당하는 스터디가 없습니다.");
        }
        return byPath;
    }

    public void updateStudyDescription(Study study, StudyDescriptionForm studyDescriptionForm) {
        modelMapper.map(studyDescriptionForm, study);
    }

    public void getStudyImage(Study study, String image) {
        study.setImage(image);
    }

    public void studyBannerEnable(Study study) {
        study.setUseBanner(true);
    }

    public void studyBannerDisable(Study study) {
        study.setUseBanner(false);
    }

    public Study getStudyUpdateTag(Account account, String path) {
        Study accountWithTagsByPath = repository.findAccountWithTagsByPath(path);
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

    // study start
    public Study getStudyToUpdateZone(Account account, String path) {
        Study accountWithZonesByPath = repository.findAccountWithZonesByPath(path);
        checkExistStudy(path, accountWithZonesByPath);
        checkManager(account, accountWithZonesByPath);

        return accountWithZonesByPath;
    }

    public void addZone(Study studyToUpdateZone, Zone zone) {
        log.info("추가 된 지역 : " + zone );
        studyToUpdateZone.getZones().add(zone);

    }

    public void removeZone(Study studyToUpdateZone, Zone zone) {
        studyToUpdateZone.getZones().remove(zone);
    }
    // study end

    private void checkManager(Account account, Study accountWithTagsByPath) {
        if (!account.isManager(accountWithTagsByPath)) throw new AccessDeniedException("해당 기능을 사용할 권한이 없습니다.");

    }

    private void checkExistStudy(String path, Study accountWithTagsByPath) {
        if (accountWithTagsByPath == null) throw new IllegalArgumentException(path + "해당 하는 스터디가 없습니다.");
    }

    public void publish(Study studyUpdate) {
        studyUpdate.publish();
    }
}
