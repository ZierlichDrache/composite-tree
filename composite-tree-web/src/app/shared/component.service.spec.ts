import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ComponentService } from './component.service';
import { ComponentNode } from './dtos';

describe('ComponentService', () => {

  let service: ComponentService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(ComponentService);
    http = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call api when getRootComponent is called', () => {

    // given
    const url = '/component/root';

    // when
    service.getRootComponent().subscribe();
    const req = http.expectOne(url);

    // then
    expect(req.request.url).toBe(url);
    expect(req.request.method).toBe('GET');
  });

  it('should call api when deleteComponent is called', () => {

     // given
     const id = 0;
     const url = `/component/${id}`;

     // when
     service.deleteComponent(id).subscribe();
     const req = http.expectOne(url);

     // then
     expect(req.request.url).toBe(url);
     expect(req.request.method).toBe('DELETE');
  });

  it('should call api when addNewLeafToComponent is called', () => {

    // given
    const id = 0;
    const url = `/component/${id}/create-leaf`;

    // when
    service.addNewLeafToComponent(id).subscribe();
    const req = http.expectOne(url);

    // then
    expect(req.request.url).toBe(url);
    expect(req.request.method).toBe('POST');
  });

  it('should call api when updateCompnentValue is called', () => {

    // given
    const id = 0;
    const url = `/component/${id}`;

    // when
    service.updateCompnentValue(id, 0).subscribe();
    const req = http.expectOne(url);

    // then
    expect(req.request.url).toBe(url);
    expect(req.request.method).toBe('PUT');
  });

  it('should call api when updateRootComponent is called', () => {

    // given
    const url = '/component/root';

    // when
    service.updateRootComponent({} as ComponentNode).subscribe();
    const req = http.expectOne(url);

    // then
    expect(req.request.url).toBe(url);
    expect(req.request.method).toBe('PUT');
  });
});
