import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticattionService } from 'src/app/service/authenticattion.service';

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {

    constructor(
        private authenticattionService: AuthenticattionService,
        private router: Router) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

        return this.isUserLoggedIn();
    }

    private isUserLoggedIn(): boolean {
        if(this.authenticattionService.isUserLoggedIn()) {
            return true;
        }
        this.router.navigate([`${this.authenticattionService.loginPath}`])
        // TODO - Sendo Notification to User
        return false;
    }
  
}
