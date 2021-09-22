import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { AuthenticattionService } from 'src/app/service/authenticattion.service';
import { NotificationService } from 'src/app/service/notification.service';

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {

    constructor(
        private authenticattionService: AuthenticattionService,
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
                        `You need to log in to access this page`.toUpperCase());
        return false;
    }
  
}
