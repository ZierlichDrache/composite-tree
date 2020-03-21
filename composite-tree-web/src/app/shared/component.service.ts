import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ComponentNode } from './component-node';

@Injectable({
  providedIn: 'root'
})
export class ComponentService {

  constructor(private readonly http: HttpClient) { }

  getRootComponent(): Observable<ComponentNode> {
    return this.http.get<ComponentNode>('/component/root');
  }

  deleteComponent(id: number): Observable<any> {
    return  this.http.delete('/component/' + id);
  }
}
