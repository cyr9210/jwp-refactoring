package kitchenpos.application.unit;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹 등록")
    @Test
    public void 메뉴그룹_등록_확인() throws Exception {
        //given
        MenuGroup returnMenuGroup = 메뉴그룹_등록됨(1L, "치킨");
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(returnMenuGroup);

        //when
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("치킨");
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);

        //then
        assertThat(menuGroupResponse.getId()).isNotNull();
    }

    @DisplayName("메뉴그룹 목록 조회")
    @Test
    public void 메느그룹목록_조회_확인() throws Exception {
        //given
        MenuGroup menuGroup1 = 메뉴그룹_등록됨(1L, "치킨");
        MenuGroup menuGroup2 = 메뉴그룹_등록됨(2L, "주류");
        MenuGroup menuGroup3 = 메뉴그룹_등록됨(3L, "사이드");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(menuGroup1, menuGroup2, menuGroup3));

        //when
        List<MenuGroupResponse> menuGroupResponse = menuGroupService.list();

        //then
        assertThat(menuGroupResponse.size()).isEqualTo(3);
    }

    public static MenuGroup 메뉴그룹_등록됨(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup(id, name);
        return menuGroup;
    }
}
