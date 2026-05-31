package com.zima.zimasocial.context.account.infastructure.controller;

import com.zima.zimasocial.context.social.author.api.view.AuthorView;
import com.zima.zimasocial.context.account.service.AccountService;
import com.zima.zimasocial.context.account.value.DeleteReason;
import com.zima.zimasocial.context.account.value.DisableReason;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    final AccountService accountService;
    @PatchMapping("/me/make-account-public")
    public ResponseEntity<AuthorView> makeAccountPublic() {
        accountService.makeAccountPublic();
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/me/make-account-private")
    public ResponseEntity<AuthorView> makeAccountPrivate() {
        accountService.makeAccountPrivate();
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/disable-account")
    public ResponseEntity<Void> disableAccount(@RequestParam(name = "reason") DisableReason reason) {
        accountService.disableAccount(reason);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/delete-account")
    public ResponseEntity<Void> deleteAccount(@RequestParam(name = "reason") DeleteReason reason) {
        accountService.deleteAccount(reason);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/accept-terms-of-use")
    public ResponseEntity<Void> acceptTermsOfUse() {
        accountService.acceptTermsOfUse();
        return ResponseEntity.noContent().build();
    }
}
