package tw.waterball.judgegirl.springboot.problem.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tw.waterball.judgegirl.entities.problem.JudgePluginTag;
import tw.waterball.judgegirl.plugins.api.JudgeGirlPluginLocator;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/plugins")
@AllArgsConstructor
public class PluginController {
    private final JudgeGirlPluginLocator pluginLocator;

    @GetMapping
    public List<JudgePluginTag> getJudgePluginTags(@RequestParam(required = false) String type) {
        Predicate<JudgePluginTag> predicate = type == null ? (tag) -> true :
                tag -> tag.getType().toString().equalsIgnoreCase(type);
        return pluginLocator.getAll().stream()
                .filter(predicate).collect(toList());
    }
}
