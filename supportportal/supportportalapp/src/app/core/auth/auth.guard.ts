import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { EnumMessages } from 'src/app/enums/enum-messages.enum';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { AuthenticationService } from 'src/app/service/authenticattion.service';
import { NotificationService } from 'src/app/service/notification.service';

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {

    constructor(
        private authenticattionService: AuthenticationService,
        private router: Router,
        private notificationService: NotificationService) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

        return this.isUserLoggedIn();
    }

    private isUserLoggedIn(): boolean {
        if(this.authenticattionService.isUserLoggedIn()) {
            return true;
        }
        this.router.navigate([`${this.authenticattionService.loginPath}`])
        this.notificationService
                .notify(NotificationType.ERROR, 
                        `${EnumMessages.FORBIDEN_MESSAGE}`);
        return false;
    }
  
}
