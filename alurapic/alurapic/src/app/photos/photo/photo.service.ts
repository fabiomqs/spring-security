import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const API = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class PhotoService {

    constructor(private http:HttpClient) { }

    listFromUser(username: string):Observable<Object[]> {
        return this.http
                .get<Object[]>(`${API}/api/v1/photos/${username}`)
    }
}
