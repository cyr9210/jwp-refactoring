package menu.ui;

import java.net.URI;
import java.util.List;
import menu.application.MenuGroupService;
import menu.dto.MenuGroupRequestDto;
import menu.dto.MenuGroupResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponseDto> create(@RequestBody final MenuGroupRequestDto request) {
        MenuGroupResponseDto created = menuGroupService.create(request);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponseDto>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list())
                ;
    }
}