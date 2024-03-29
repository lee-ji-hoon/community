package com.community;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.account.AccountType;
import com.community.domain.board.BoardRepository;
import com.community.domain.council.CouncilRepository;
import com.community.domain.graduation.GraduationRepository;
import com.community.domain.market.MarketRepository;
import com.community.domain.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final AccountRepository accountRepository;
    private final CouncilRepository councilRepository;
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;
    private final MarketRepository marketRepository;
    private final StudyRepository studyRepository;
    private final GraduationRepository graduationRepository;

    @PostConstruct
    public void init() {
        String make = RandomString.make(5);
        String BOARD_CONTENT_VALUE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,\n" +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n" +
                "Ut enim ad minim veniam,\n" +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
                "Duis aute irure dolor in\n" +
                "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\n" +
                "Excepteur sint occaecat cupidatat non proident,\n" +
                "sunt in culpa qui officia deserunt mollit anim id est laborum.";
        String BOARD_CONTENT_VALUE2 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,\n" +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n" +
                "Ut enim ad minim veniam,\n" +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
                "Duis aute irure dolor in\n" +
                "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\n" +
                "Excepteur sint occaecat cupidatat non proident,\n" +
                "sunt in culpa qui officia deserunt mollit anim id est laborum.";
        String COUNCIL_CONTENT_VALUE = "<p><span style=\"font-family: inherit;\">안녕하십니까 멀티미디어학과 학우분들!</span><br></p><p>제 27대 학생회 CROCE 입니다.</p><p><br></p><p>코로나 19로 인해 동기 또는 선후배와 친해질 기회가 없어&nbsp;</p><p>아쉬움이 많은 학우분들을 위해 친목도모 행사를 준비했습니다.&nbsp;</p><p><br></p><p>학회비를 납부한 전 학년을 대상으로 랜덤으로 팀을 짠 후, 학회비 납부자에게는 별도의 지원금이 지급될 예정입니다.</p><p>\uD83D\uDCCD학회비 미납부자는 10,000원의 참가비를 내셔야 합니다!</p><p><br></p><p>\uD83C\uDFF7 행사날짜: 3/23&nbsp;</p><p>\uD83D\uDCC6 신청기간: 3/7~3/14</p><p>\uD83D\uDC64 신청대상: 멀티미디어학과 전체 학년</p><p>\uD83D\uDD17 신청방법: 네이버폼 제출</p><p><br></p><p>멀티미디어학과의 친목을 다질 수 있는 소중한 자리에 학우분들의 많은 참여 부탁드립니다!&nbsp;</p><p>행사 참가 신청은 각 학년 공지방에 올린 네이버폼을 이용해주세요.</p><p>\uD83D\uDCCD시간과 장소는 추후 공지할 예정입니다!</p><p>\uD83D\uDC89코로나 19 방역수칙에 준수하여 진행될 예정입니다!</p><p><br></p><p>\"친해지길 바라\"라는 행사 이름처럼 학우분들이 친해지는 계기가 되길 바랍니다! 항상 멀티미디어 학우분들을 위해 노력하는 CROCE가 되겠습니다. \uD83D\uDC9B</p>";
        String COUNCIL_CONTENT_VALUE1 = "<p><span style=\"font-family: inherit;\">멀티미디어학과 전학년 학우 여러분들 안녕하십니까 제 26대 학생회 CROCE입니다.</span><br></p><p><br></p><p>벌써 2021년 마지막 간식행사가 다가왔습니다. 마지막 간식행사인 만큼 많은 관심과 참여 부탁드립니다.</p><p><br></p><p>자세한 사항은 사진을 참고해 주시길 부탁드리며 학과 학회비를 납부한 학우만 참여 가능하니 유의해주시길 바랍니다.</p><p><br></p><p>쿠폰 사용 기간은 쿠폰에 명시되어 있어 쿠폰 지급시 확인 부탁드립니다.</p><p><br></p><p>항상 멀티미디어학과 학우분들을 위해 노력하는 학생회 CROCE가 되겠습니다! : )</p><p><br></p><p>모든 홍보물은 인스타그램에도 업로드될 예정이오니 멀티미디어학과 학생회 공식 인스타그램(@nsu_multi)팔로우 부탁드립니다❤</p>";
        String COUNCIL_CONTENT_VALUE2 = "<p>◘ 2022-1학기 캡스톤 디자인(종합 설계) 운영 지침 및 과제 신청 안내 (4학년 필독!!)</p><p><br></p><p>안녕하세요. 멀티미디어학과 행정조교입니다.</p><p>현재 2022-1학기 캡스톤 디자인(종합 설계) 운영 지침 및 과제 신청 마감이 얼마 남지 않았습니다. 3월 15일(화요일) 17:30까지 학과 사무실로 확인받은 후(*지도교수님 확인 포함) 취업지원처로 직접제출 하시면 됩니다.&nbsp;</p><p><br></p><p>☞취업지원처 -&gt; 학생복지회관 1층 김정현 선생님(캡스톤 담당)</p><p><br></p><p>☞기간엄수!!&nbsp;</p><p>☞그 전에 신청하지 못할 시 지원금도 받지 못하오니 주의부탁드립니다.</p><p><br></p><p><br></p><p>-학과홈페이지 및 4학년 대상으로 문자 또는 안내 2월 초에 공지 했었음을 알려드립니다.</p><p>-양식 및 신청서는 학과홈페이지 확인 후 참고 부탁드립니다.</p>";
        String COUNCIL_CONTENT_VALUE3 = "<p>안녕하세요?</p><p>남서울대학교 학생처 장학담당 이유경주임입니다.</p><p>다름이 아니라 22년1학기 국가장학금 신청이</p><p>3월 16일 마감됩니다. 현재 우리대학 학생들의</p><p>신청률이 저조하여 각 학과별 학회장께서는</p><p>학과별 학년별 단톡방, 공식적인 안내창구에</p><p>아래의 내용을 홍보 부탁드립니다.</p><p>(특히 새로 입학한 신편입생들에게 적극&nbsp;</p><p>&nbsp;홍보 부탁드립니다.)</p><p><br></p><p>&lt;안내 내용&gt;</p><p><br></p><p>∙ 2022.1학기 국가장학금 2차 신청 안내</p><p><br></p><p><br></p><p>1. 신청일정: 2022.2.3.(목) 9시 ~ 3.16.(수) 18시(총 42일간)</p><p><br></p><p>2. 서류제출 및 가구원 동의: 2022.2.3.(목) ~ 3.18.(금)18시</p><p><br></p><p>3. 신청대상: 재학생, 신입생, 편입생, 재입학생, 복학생 등 모든 학적 신청 가능</p><p>&nbsp;※ 재학생은 1차 신청이 원칙이며, 재학 중 2회에 한하여 구제신청 자동 적용 및 심사 후 지원 가능</p><p>&nbsp;</p><p>4. 신청방법: 한국장학재단 홈페이지와 모바일 앱을 통해 신청 가능</p><p>&nbsp;가. 홈페이지 주소: www.kosaf.go.kr\\</p><p>&nbsp;나. 모바일 앱 주소: mo.kosaf.go.kr/apps</p><p><br></p><p>ㅇ (신청문의)한국장학재단 고객센터 1599-2000</p><p>&nbsp;※ 자세한 사항은 한국장학재단 홈페이지 참조 바람.</p>";

        String FORUM_TEST_CONTENT_VALUE1 = "<p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><span style=\"font-size: 18pt;\"><b><span style=\"color: rgb(0, 0, 0);\">Spring Framework란?</span></b></span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><span style=\"font-size: 12pt; color: rgb(0, 0, 0);\">자바 플랫폼을 위한 오픈소스 애플리케이션 프레임워크로서 엔터프라이즈급 애플리케이션을 개발하기 위한 모든 기능을 종합적으로 제공하는 경량화된 솔루션입니다.</span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><span style=\"font-size: 12pt; color: rgb(0, 0, 0);\">엔터프라이즈급 개발이란 뜻대로만 풀이하면 기업을 대상으로 하는 개발이라는 말입니다.&nbsp;</span><span style=\"font-size: 12pt; color: rgb(0, 0, 0);\">즉, 대규모 데이터 처리와 트랜잭션이 동시에 여러 사용자로 부터 행해지는 매우 큰 규모의 환경을 엔터프라이즈 환경이라 일컫습니다.</span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><b><span style=\"color: rgb(255, 0, 0);\">Spirng Framework는 경량 컨테이너로 자바 객체를 담고 직접 관리</span></b><span style=\"color: rgb(0, 0, 0);\">합니다.&nbsp;객체의 생성 및&nbsp;소멸 그리고&nbsp;라이프 사이클을관리하며&nbsp;언제든 Spring 컨테이너로 부터 필요한 객체를 가져와 사용할 수 있습니다.&nbsp;</span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><span style=\"color: rgb(0, 0, 0);\">이는 Spirng이 IOC 기반의 Framework임을 의미합니다.</span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\">&nbsp;</p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><span style=\"font-size: 18pt;\"><b><span style=\"color: rgb(0, 0, 0);\">Spring Framework는 IOC기반이다. IOC란?</span></b></span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><span style=\"font-size: 12pt;\">&nbsp;</span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><span style=\"font-size: 12pt; color: rgb(0, 0, 0);\">IOC는&nbsp;</span><span style=\"color: rgb(0, 0, 0);\">Inversion of Control의 약자로 말 그대로 제어의 역전입니다.&nbsp;그럼 제어의 역전이란 무엇일까요?&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\">&nbsp;</p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><span style=\"color: rgb(0, 0, 0);\">일반적으로 지금까지&nbsp;프로그램은</span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><span style=\"color: rgb(0, 0, 0);\">객체 결정 및 생성 -&gt; 의존성 객체 생성 -&gt; 객채 내의 메소드 호출 하는 작업을 반복했습니다.&nbsp;</span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><span style=\"color: rgb(0, 0, 0);\">이는 각 객체들이 프로그램의 흐름을 결정하고 각 객체를 구성하는 작업에 직접적으로 참여한 것입니다.</span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><span style=\"color: rgb(255, 0, 0);\"><b>즉, 모든 작업을 사용자가 제어하는 구조인 것입니다.</b></span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\">&nbsp;</p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><span style=\"font-size: 12pt; color: rgb(0, 0, 0);\">하지만 IOC에서는 이 흐름의 구조를 바꿉니다. IOC에서의</span><span style=\"font-size: 12pt; color: rgb(0, 0, 0);\">&nbsp;객체는 자기가 사용할 객체를 선택하거나 생성하지 않는다. 또한 자신이 어디서 만들어지고 어떻게 사용되는지 또한 모릅니다. 자신의 모든 권한을 다른 대상에 위임함으로 써 제어권한을 위임받은 특별한 객체에 의해 결정되고 만들어집니다.</span></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><b><span style=\"font-size: 12pt; color: rgb(255, 0, 0);\">즉,&nbsp;</span><span style=\"font-size: 12pt; color: rgb(255, 0, 0);\">제어의 흐름을 사용자가 컨트롤 하지 않고 위임한 특별한 객체에 모든 것을 맡기는 것입니다.</span></b></p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\">&nbsp;</p><p data-ke-size=\"size16\" style=\"margin-bottom: 20px; padding-right: 0px; padding-left: 0px; color: rgb(51, 51, 51); font-family: &quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif; font-size: 16px; padding-top: 0px !important; padding-bottom: 0px !important;\"><b><span style=\"color: rgb(255, 0, 0);\">IOC란 기존 사용자가 모든 작업을 제어하던 것을&nbsp;특별한 객체에 모든 것을 위임하여&nbsp;객체의 생성부터 생명주기 등 모든 객체에 대한 제어권이 넘어 간 것을 IOC, 제어의 역전 이라고 합니다.</span></b></p>";
        String FORUM_TEST_CONTENT_VALUE3 = "<h2 id=\"spring\" style=\"margin-top: 2em; margin-bottom: 0.5em; line-height: 1.2; font-family: -apple-system, &quot;system-ui&quot;, Roboto, &quot;Segoe UI&quot;, &quot;Helvetica Neue&quot;, &quot;Lucida Grande&quot;, Arial, sans-serif; font-weight: bold; font-size: 1.25em; transition: all 0.2s ease-in-out 0s; padding-bottom: 0.5em; border-bottom-width: 1px; border-bottom-color: rgb(242, 243, 243); color: rgb(61, 65, 68);\">Spring<a class=\"header-link\" href=\"http://dawoonjeong.com/spring-spring_mvc-vs-spring_boot-vs-spring_mvc/#spring\" title=\"Permalink\" style=\"color: rgb(47, 125, 149); text-decoration: none; transition: opacity 0.2s ease-in-out 0.1s; position: relative; left: 0.5em; opacity: 0; font-size: 0.8em;\"><span class=\"sr-only\" style=\"transition: all 0.2s ease-in-out 0s;\">Permalink</span><span class=\"fas fa-link\" style=\"transition: all 0.2s ease-in-out 0s; -webkit-font-smoothing: antialiased; display: inline-block; font-variant-numeric: normal; font-variant-east-asian: normal; text-rendering: auto; line-height: 1; font-family: &quot;Font Awesome 5 Free&quot;;\"></span></a></h2><ul style=\"orphans: 3; widows: 3; color: rgb(61, 65, 68); font-family: -apple-system, &quot;system-ui&quot;, Roboto, &quot;Segoe UI&quot;, &quot;Helvetica Neue&quot;, &quot;Lucida Grande&quot;, Arial, sans-serif; font-size: 18px;\"><li style=\"margin-bottom: 0.5em; font-size: 1em;\">오픈 소스 경량 프레임 워크</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">자바 개발자가 간단하고 안정적이며 확장 가능한 엔터프라이즈 애플리케이션을 빌드 할 수 있도록 함</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">비즈니스 개체를 관리하는 데 도움이되는 다양한 방법을 제공하는 데 중점을 둠</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Java 데이터베이스 연결 (JDBC), JavaServer Pages (JSP) 및 Java Servlet과 같은 기존 Java 프레임 워크 및 API (Application Programming Interface)에 비해 웹 애플리케이션 개발이 훨씬 쉬워짐</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">AOP (Aspect-Oriented Programming), POJO (Plain Old Java Object) 및 DI (dependency injection)와 같은 다양한 새로운 기술을 사용하여 엔터프라이즈 애플리케이션을 개발<ul style=\"margin-top: 0.5em;\"><li style=\"margin-bottom: 0.5em; font-size: 1em;\">스프링 AOP 같은 서브 프레임 워크라고도 층의 집합</li></ul></li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Spring 개체 관계형 매핑 (Spring ORM). Spring Web Flow 및 Spring Web MVC 등이 있음<ul style=\"margin-top: 0.5em;\"><li style=\"margin-bottom: 0.5em; font-size: 1em;\">웹 응용 프로그램을 구성하는 동안 이러한 모듈을 별도로 사용 가능</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">모듈은 웹 응용 프로그램에서 더 나은 기능을 제공하기 위해 함께 그룹화 가능</li></ul></li></ul><h2 id=\"spring-mvc\" style=\"margin-top: 2em; margin-bottom: 0.5em; line-height: 1.2; font-family: -apple-system, &quot;system-ui&quot;, Roboto, &quot;Segoe UI&quot;, &quot;Helvetica Neue&quot;, &quot;Lucida Grande&quot;, Arial, sans-serif; font-weight: bold; font-size: 1.25em; transition: all 0.2s ease-in-out 0s; padding-bottom: 0.5em; border-bottom-width: 1px; border-bottom-color: rgb(242, 243, 243); color: rgb(61, 65, 68);\">Spring MVC<a class=\"header-link\" href=\"http://dawoonjeong.com/spring-spring_mvc-vs-spring_boot-vs-spring_mvc/#spring-mvc\" title=\"Permalink\" style=\"color: rgb(47, 125, 149); text-decoration: none; transition: opacity 0.2s ease-in-out 0.1s; position: relative; left: 0.5em; opacity: 0; font-size: 0.8em;\"><span class=\"sr-only\" style=\"transition: all 0.2s ease-in-out 0s;\">Permalink</span><span class=\"fas fa-link\" style=\"transition: all 0.2s ease-in-out 0s; -webkit-font-smoothing: antialiased; display: inline-block; font-variant-numeric: normal; font-variant-east-asian: normal; text-rendering: auto; line-height: 1; font-family: &quot;Font Awesome 5 Free&quot;;\"></span></a></h2><ul style=\"orphans: 3; widows: 3; color: rgb(61, 65, 68); font-family: -apple-system, &quot;system-ui&quot;, Roboto, &quot;Segoe UI&quot;, &quot;Helvetica Neue&quot;, &quot;Lucida Grande&quot;, Arial, sans-serif; font-size: 18px;\"><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Spring은 확장 가능한 애플리케이션을 만드는 데 널리 사용 되는 Spring MVC 프레임 워크를 제공</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Spring MVC 프레임 워크는 Model View, Controller라는 모듈의 분리를 가능하게하고 애플리케이션 통합을 원활하게 처리</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">개발자는 일반 Java 클래스를 사용하여 복잡한 응용 프로그램을 만들 수 있음</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">모델 객체는 맵을 사용하여 뷰와 컨트롤러간에 전달</li></ul><h2 id=\"spring-boot\" style=\"margin-top: 2em; margin-bottom: 0.5em; line-height: 1.2; font-family: -apple-system, &quot;system-ui&quot;, Roboto, &quot;Segoe UI&quot;, &quot;Helvetica Neue&quot;, &quot;Lucida Grande&quot;, Arial, sans-serif; font-weight: bold; font-size: 1.25em; transition: all 0.2s ease-in-out 0s; padding-bottom: 0.5em; border-bottom-width: 1px; border-bottom-color: rgb(242, 243, 243); color: rgb(61, 65, 68);\">Spring Boot<a class=\"header-link\" href=\"http://dawoonjeong.com/spring-spring_mvc-vs-spring_boot-vs-spring_mvc/#spring-boot\" title=\"Permalink\" style=\"color: rgb(47, 125, 149); text-decoration: none; transition: opacity 0.2s ease-in-out 0.1s; position: relative; left: 0.5em; opacity: 0; font-size: 0.8em;\"><span class=\"sr-only\" style=\"transition: all 0.2s ease-in-out 0s;\">Permalink</span><span class=\"fas fa-link\" style=\"transition: all 0.2s ease-in-out 0s; -webkit-font-smoothing: antialiased; display: inline-block; font-variant-numeric: normal; font-variant-east-asian: normal; text-rendering: auto; line-height: 1; font-family: &quot;Font Awesome 5 Free&quot;;\"></span></a></h2><ul style=\"orphans: 3; widows: 3; color: rgb(61, 65, 68); font-family: -apple-system, &quot;system-ui&quot;, Roboto, &quot;Segoe UI&quot;, &quot;Helvetica Neue&quot;, &quot;Lucida Grande&quot;, Arial, sans-serif; font-size: 18px;\"><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Spring Boot는 기존의 스프링 프레임 워크 위에 구축 (스프링 프레임 워크 기반)</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">스프링의 모든 기능을 제공하면서도 스프링보다 사용하기 쉬움</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Spring Boot는 마이크로 서비스 기반 프레임 워크이며 매우 짧은 시간에 프로덕션 준비 애플리케이션을 만듦</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Spring Boot에서는 모든 것이 자동으로 구성됨</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">특정 기능을 활용하기 위해 적절한 구성을 사용하기 만</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Spring Boot는 REST API를 개발하려는 경우 매우 유용</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Spring Boot는 프로젝트를 war 또는 jar 파일로 변환하는 기능을 제공</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Tomcat의 인스턴스는 클라우드에서도 실행 가능</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">자주 사용하는 라이브러리가 미리 조합되어있음</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">복잡한 설정이 자동 처리됨</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">내장서버를 포함 (톰캣) 서버를 추가로 설치하지 않아도 바로 개발 가능</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">톰캣, 제티와 같은 WAS에 배포하지 않고도 실행할 수 있는 jar 파일로 웹어플리케이션 개발 가능</li></ul><h3 id=\"spring-boot의-4계층\" style=\"margin-top: 2em; margin-bottom: 0.5em; line-height: 1.2; font-family: -apple-system, &quot;system-ui&quot;, Roboto, &quot;Segoe UI&quot;, &quot;Helvetica Neue&quot;, &quot;Lucida Grande&quot;, Arial, sans-serif; font-weight: bold; font-size: 1.125em; color: rgb(61, 65, 68);\">Spring boot의 4계층<a class=\"header-link\" href=\"http://dawoonjeong.com/spring-spring_mvc-vs-spring_boot-vs-spring_mvc/#spring-boot%EC%9D%98-4%EA%B3%84%EC%B8%B5\" title=\"Permalink\" style=\"color: rgb(47, 125, 149); text-decoration: none; transition: opacity 0.2s ease-in-out 0.1s; position: relative; left: 0.5em; opacity: 0; font-size: 0.8em;\"><span class=\"sr-only\" style=\"transition: all 0.2s ease-in-out 0s;\">Permalink</span><span class=\"fas fa-link\" style=\"transition: all 0.2s ease-in-out 0s; -webkit-font-smoothing: antialiased; display: inline-block; font-variant-numeric: normal; font-variant-east-asian: normal; text-rendering: auto; line-height: 1; font-family: &quot;Font Awesome 5 Free&quot;;\"></span></a></h3><ul style=\"orphans: 3; widows: 3; color: rgb(61, 65, 68); font-family: -apple-system, &quot;system-ui&quot;, Roboto, &quot;Segoe UI&quot;, &quot;Helvetica Neue&quot;, &quot;Lucida Grande&quot;, Arial, sans-serif; font-size: 18px;\"><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Presentation Layer : 이름에서 알 수 있듯이 view (예 : front-end 부분)로 구성</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Data Access Layer : 데이터베이스에 대한 CRUD (create, retrieve, update, delete)</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Service Layer : 서비스 클래스로 구성되며 데이터 액세스 계층에서 제공하는 서비스를 사용</li><li style=\"margin-bottom: 0.5em; font-size: 1em;\">Integration Layer : 웹별 웹 서비스 (인터넷을 통해 사용 가능한 모든 서비스 및 XML 메시징 시스템 사용)로 구성</li></ul>";

        if (!accountRepository.existsByStudentId("17-100000")) {
            accountRepository.save(new Account(null, "testerA@nsu.ac.kr", "testerA", "17-100000", "테스터A", passwordEncoder.encode("test1234!"),
                    true, "asdf", null, null, LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1),
                    null, null, null, null, null, null, null, null, true, true, true, true,
                    true, true, true, null, AccountType.ROLE_ADMIN));
            /*boardRepository.save(new Board(null, "자유", "", "수강신청 다들 성공기원🙏", null,null,  null, false,0,BOARD_CONTENT_VALUE, accountRepository.findByEmail("test@naver.com"), 0, LocalDateTime.now().minusMinutes(20), null));
            boardRepository.save(new Board(null, "질문", "college", "수강신청은 어떻게 하나요?",null, null, null, false,0,"ㅈㄱㄴ", accountRepository.findByEmail("test@naver.com"), 0, LocalDateTime.now().minusSeconds(40), null));
            boardRepository.save(new Board(null, "정보", "java", "스프링 MVC 모델과 스프링 부트에 대한 설명","스프링은 어렵습니다.", null, null, false,4,FORUM_TEST_CONTENT_VALUE1, accountRepository.findByEmail("test@naver.com"), 0, LocalDateTime.now().minusSeconds(40), null));*/
        }

        if (!accountRepository.existsByStudentId("17-100001")) {
            accountRepository.save(new Account(null, "testerB@nsu.ac.kr", "testerB", "17-100001", "테스터B", passwordEncoder.encode("test1234!"),
                    true, "asdfg", null, null, LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1),
                    null, null, null, null, null, null, null, null, true, true, true, true,
                    true, true, true, null, AccountType.ROLE_USER));
            /*boardRepository.save(new Board(null, "자유", "", "수강신청 다들 성공기원🙏", null,null,  null, false,0,BOARD_CONTENT_VALUE, accountRepository.findByEmail("test@naver.com"), 0, LocalDateTime.now().minusMinutes(20), null));
            boardRepository.save(new Board(null, "질문", "college", "수강신청은 어떻게 하나요?",null, null, null, false,0,"ㅈㄱㄴ", accountRepository.findByEmail("test@naver.com"), 0, LocalDateTime.now().minusSeconds(40), null));
            boardRepository.save(new Board(null, "정보", "java", "스프링 MVC 모델과 스프링 부트에 대한 설명","스프링은 어렵습니다.", null, null, false,4,FORUM_TEST_CONTENT_VALUE1, accountRepository.findByEmail("test@naver.com"), 0, LocalDateTime.now().minusSeconds(40), null));*/
        }

        /*if (!accountRepository.existsByStudentId("17-100424")) {
            accountRepository.save(new Account(null, "dlwlgns1240@nsu.ac.kr", "ezhoon", "17-100424", "이지훈", passwordEncoder.encode("12401240"),
                    true, "asdf1", null, null, LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1),
                    "잘 부탁드립니다", "https://github.com/lee-ji-hoon", "대학생", "서울, 용산", null,null,null, true, true,
                    true, true, true, true, null));
            *//*boardRepository.save(new Board(null, "자유", "", "오늘 학식은 뭔가요?", null, null, null, false, 0,BOARD_CONTENT_VALUE, accountRepository.findByEmail("dlwlgns1240@naver.com"), 0, LocalDateTime.now().minusHours(1), null));*//*
        }*/

        /*if (!accountRepository.existsByStudentId("17-100425")) {
            accountRepository.save(new Account(null, "tester3@nsu.ac.kr", "tester3", "17-100425", "테스터", passwordEncoder.encode("12401240"),
                    true, "asdf1", null, null, LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1),
                    "잘 부탁드립니다", "https://github.com/lee-ji-hoon", "대학생", "서울, 용산", null, null, null, null, true, true,
                    true, true, true, true, null, 0));
//            boardRepository.save(new Board(null, "정보", "java", "스프링에 대한 설명", "스프링은 과연 무엇인가..", null, null, false, 0,FORUM_TEST_CONTENT_VALUE3, accountRepository.findByEmail("test1@naver.com"), 0, LocalDateTime.now().minusHours(1), null));
        }*/

        /*if (!accountRepository.existsByStudentId("17-100444")) {
            accountRepository.save(new Account(null, "kawnsdud@nsu.ac.kr", "jwhy", "17-100444", "준영", passwordEncoder.encode("test1234!"),
                    true, "asdf12", null, null, LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1),
                    "hello", "https://github.com/Jwhyee", "대학생", "경기도, 수원", null, null, null, true, true,
                    true, true, true, true, null));
            *//*boardRepository.save(new Board(null, "자유", "", "밥 먹으러 갈사람 9함",null, null, null, false, 0,FORUM_TEST_CONTENT_VALUE3, accountRepository.findByEmail("tester0@naver.com"), 0, LocalDateTime.now().minusHours(1), null));*//*
        }*/

        if (!accountRepository.existsByStudentId("11-111111")) {
            accountRepository.save(new Account(null, "croce@nsu.ac.kr", "CROCE", "11-111111", "학생회", passwordEncoder.encode("mmult1234!"),
                    true, "asdf123", null, null, LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1),
                    "안녕하세요 27대 학생회 CROCE 입니다.", "", "대학생", "충청북도, 성환", null, null, null, null, true, true,
                    true, true, true, true,  true, null, AccountType.ROLE_COUNCIL));
            /*councilRepository.save(new Council(null, "행사", "💛친해지길 바라💛", "전체 학년", "http://naver.me/G3K7QAeA", "010-1234-1234", COUNCIL_CONTENT_VALUE, accountRepository.findByEmail("croce@naver.com"), 0, LocalDate.of(2022, 3, 23), LocalDate.of(2022, 3, 23), LocalDate.of(2022, 3, 7), LocalDate.of(2022, 3, 14), LocalDateTime.now()));
            councilRepository.save(new Council(null, "행사", "🍦기말 간식행사🍦", "전체 학년", null, "010-1234-1234", COUNCIL_CONTENT_VALUE1, accountRepository.findByEmail("croce@naver.com"), 0, LocalDate.of(2021, 12, 5), LocalDate.of(2021, 12, 5), LocalDate.of(2021, 11, 29), LocalDate.of(2021, 12, 02), LocalDateTime.now()));
            councilRepository.save(new Council(null, "공지", "2022-1학기 캡스톤 디자인(종합 설계) 운영 지침 및 과제 신청 안내", "전체 학년", "http://naver.me/G3K7QAeA", "1599-2000", COUNCIL_CONTENT_VALUE2, accountRepository.findByEmail("croce@naver.com"), 0, LocalDate.of(2022, 3, 23), LocalDate.of(2022, 3, 23), LocalDate.of(2022, 3, 7), LocalDate.of(2022, 3, 14), LocalDateTime.now()));
            councilRepository.save(new Council(null, "공지", "22년1학기 국가장학금 신청", "전체 학년", "http://naver.me/G3K7QAeA", null, COUNCIL_CONTENT_VALUE3, accountRepository.findByEmail("croce@naver.com"), 0, LocalDate.of(2022, 3, 23), LocalDate.of(2022, 3, 23), LocalDate.of(2022, 3, 7), LocalDate.of(2022, 3, 14), LocalDateTime.now()));*/
        }

        /*for (int i = 0; i < 50; i++) {
            marketRepository.save(new Market(null, null, "새것", "name" + i, 1500, "sell", null, "상세내용" + make, MarketItemStatus.SELLING, null, "https://unectbucket.s3.ap-northeast-2.amazonaws.com/market-img/3fa026fe-fdab-4cee-a3ce-6d21bbe96a66.JPG", true, LocalDateTime.now()));
            marketRepository.save(new Market(null, null, "새것", "name" + i, 1500, "buy", null, "상세내용" + make, MarketItemStatus.SHARE, null, "https://unectbucket.s3.ap-northeast-2.amazonaws.com/market-img/3fa026fe-fdab-4cee-a3ce-6d21bbe96a66.JPG", true, LocalDateTime.now()));
            marketRepository.save(new Market(null, null, "새것", "name" + i, 1500, "share", null, "상세내용" + make, MarketItemStatus.PURCHASE, null, "https://unectbucket.s3.ap-northeast-2.amazonaws.com/market-img/3fa026fe-fdab-4cee-a3ce-6d21bbe96a66.JPG", true, LocalDateTime.now()));
        }*/

        /*for (int i = 0; i < 50; i++) {
            councilRepository.save(new Council(null, "공지", make, "전체 학년", null, null, make, null, null, null, null, null, null, null, null, null, null, LocalDateTime.now()));
        }*/

        /*for (int i = 0; i < 50; i++) {
            Study save = studyRepository.save(new Study(null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null,
                    null, null, null,
                    null, 5, 10));
        }*/

        /*for (int i = 0; i < 20; i++) {
            Graduation graduation = Graduation.builder()
                    .title(i + make)
                    .teamName(make)
                    .description(make)
                    .path(make)
                    .graduationType("WEB")
                    .teamMember("이지훈")
                    .graduationDate(2019)
                    .build();
            graduationRepository.save(graduation);
        }

        for (int i = 0; i < 20; i++) {
            Graduation graduation = Graduation.builder()
                    .title(i + make)
                    .teamName(make)
                    .description(make)
                    .path(make)
                    .graduationType("VIDEO")
                    .teamMember("가준영")
                    .graduationDate(2020)
                    .build();
            graduationRepository.save(graduation);
        }

        for (int i = 0; i < 20; i++) {
            Graduation graduation = Graduation.builder()
                    .title(i + make)
                    .teamName(make)
                    .description(make)
                    .path(make)
                    .graduationType("MOBILE")
                    .teamMember("가준영")
                    .graduationDate(2021)
                    .build();
            graduationRepository.save(graduation);
        }

        for (int i = 0; i < 20; i++) {
            Graduation graduation = Graduation.builder()
                    .title(i + make)
                    .teamName(make)
                    .description(make)
                    .path(make)
                    .graduationType("VR_AR")
                    .teamMember("가준영")
                    .graduationDate(2022)
                    .build();
            graduationRepository.save(graduation);
        }

        for (int i = 0; i < 20; i++) {
            Graduation graduation = Graduation.builder()
                    .title(i + make)
                    .teamName(make)
                    .description(make)
                    .path(make)
                    .graduationType("기타")
                    .teamMember("가준영")
                    .graduationDate(2023)
                    .build();
            graduationRepository.save(graduation);
        }*/


    }

}