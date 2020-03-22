import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ComponentNode } from './dtos';

/**
 * Service to call backend for the components
 */
@Injectable({
  providedIn: 'root'
})
export class ComponentService {

  constructor(private readonly http: HttpClient) { }

  getRootComponent(): Observable<ComponentNode> {
    return this.http.get<ComponentNode>('/component/root');
  }

  deleteComponent(id: number): Observable<any> {
    return  this.http.delete(`/component/${id}`);
  }

  addNewLeafToComponent(id: number): Observable<ComponentNode> {
    return this.http.post<ComponentNode>(`/component/${id}/create-leaf` , null);
  }

  updateCompnentValue(id: number, newValue: number): Observable<any> {
    const params = new HttpParams().set('newValue', newValue + '');
    return this.http.put(`/component/${id}`, params);
  }

  updateRootComponent(rootComponent: ComponentNode): Observable<ComponentNode> {
    return this.http.put<ComponentNode>('/component/root', rootComponent);
  }
}
