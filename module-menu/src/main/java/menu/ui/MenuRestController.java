package menu.ui;

import java.net.URI;
import java.util.List;
import menu.application.MenuService;
import menu.dto.MenuRequestDto;
import menu.dto.MenuResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponseDto> create(@RequestBody final MenuRequestDto request) {
        final MenuResponseDto created = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponseDto>> list() {
        return ResponseEntity.ok()
                .body(menuService.list())
                ;
    }
}