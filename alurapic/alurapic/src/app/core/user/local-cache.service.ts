import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { User } from 'src/app/model/user';
import { TokenService } from '../jwt-token/token.service';
import jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class LocalCacheService {

    private userSubject = new BehaviorSubject<User>(null);
    private username: string;

    constructor(private tokenService: TokenService) { 
        this.tokenService.hasToken && 
            this.decodeAndNotify();
    }

    setToken(token: string) {
        this.tokenService.setToken(token);
        this.decodeAndNotify();
    }

    getUser():Observable<User> {
        return this.userSubject.asObservable();
    }

    logout() {
        this.tokenService.removeToken();
        this.userSubject.next(null);
    }

    isLogged():boolean {
        return this.tokenService.hasToken();
    }

    getUsername(): string {
        return this.username;
    }

    private decodeAndNotify() {
        if(this.tokenService.hasToken()) {
            const token = this.tokenService.getToken();
            const user = jwt_decode(token) as User;
            this.username = user.username;
            this.userSubject.next(user);
        }
    }

}
