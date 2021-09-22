import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticattionService } from 'src/app/service/authenticattion.service';
import { UserService } from 'src/app/service/user.service';

@Injectable()
export class RequestInterceptor implements HttpInterceptor {

    constructor(
        private authenticattionService: AuthenticattionService,
        private userService: UserService) {}

    //exclude
    // /login, /api/v1/user/register, /api/v1/user/resetpassword/**, /api/v1/user/image/**
    intercept(request: HttpRequest<any>, handler: HttpHandler): Observable<HttpEvent<any>> {
        if(request.url.includes(`${this.authenticattionService.host}${this.authenticattionService.loginPath}`) || 
           request.url.includes(`${this.userService.host}${this.userService.apiPrefix}${this.userService.userPath}/register`) ||
           request.url.includes(`${this.userService.host}${this.userService.apiPrefix}${this.userService.userPath}resetpassword/`) || 
           request.url.includes(`${this.userService.host}${this.userService.apiPrefix}${this.userService.userPath}/image/`)) {
            
            return handler.handle(request);

        }
        this.authenticattionService.loadToken();
        const token = this.authenticattionService.getToken();
        const cloneRequest = request.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
        return handler.handle(cloneRequest);
    }
}
