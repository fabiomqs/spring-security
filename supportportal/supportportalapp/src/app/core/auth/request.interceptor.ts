import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from 'src/app/service/authenticattion.service';
import { UserService } from 'src/app/service/user.service';
import { HeaderType } from 'src/app/enums/header-type.enum';
import { EnumRoutes } from 'src/app/enums/enum-routes.enum';

@Injectable()
export class RequestInterceptor implements HttpInterceptor {

    constructor(
        private authenticattionService: AuthenticationService,
        private userService: UserService) {}

    intercept(request: HttpRequest<any>, handler: HttpHandler): Observable<HttpEvent<any>> {
        const loginPath = `${this.authenticattionService.host}${this.authenticattionService.loginPath}`;
        const regiterPath = `${this.userService.host}${this.userService.apiPrefix}${this.userService.userPath}/${EnumRoutes.REGISTER}`;
        const imagePath = `${this.userService.host}${this.userService.apiPrefix}${this.userService.userPath}/${EnumRoutes.IMAGE}`;

        if(request.url.includes(loginPath) || request.url.includes(regiterPath) || request.url.includes(imagePath)) {
            
            return handler.handle(request);

        }
        if(this.authenticattionService.isUserLoggedIn()) {
            //this.authenticattionService.loadToken();
            const token = this.authenticattionService.getToken();
            const cloneRequest = request.clone({ setHeaders: { Authorization: `${HeaderType.BEARER}${token}` } });
            return handler.handle(cloneRequest);
        } else {
            return handler.handle(request);
        }
        
    }
}
