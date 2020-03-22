import { TreeNodeComponent } from './tree-node.component';

describe('TreeNodeComponent unit', () => {
  let component: TreeNodeComponent;

  beforeEach(() => {
    component = new TreeNodeComponent();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create new value control when ngOnInit is called', () => {

    // pre check if initial undefined
    expect(component.newValue).toBeUndefined();

    // when
    component.ngOnInit();

    // then
    expect(component.newValue).toBeDefined();
  });

  it('should emit addNewNodeEvent when addNewNode is called', () => {

    // given
    spyOn(component.addNewNodeEvent, 'emit');

    // when
    component.addNewNode();

    // then
    expect(component.addNewNodeEvent.emit).toHaveBeenCalled();
  });

  it('should emit removeNodeEvent when removeThisNode is called', () => {

    // given
    spyOn(component.removeNodeEvent, 'emit');

    // when
    component.removeThisNode();

    // then
    expect(component.removeNodeEvent.emit).toHaveBeenCalled();
  });

  it('should set true for optionsVisible when showOptions is called', () => {

    // given
    component.optionsVisible = false;

    // when
    component.showOptions();

    // then
    expect(component.optionsVisible).toBe(true);
  });

  it('should set false for edit and optionsVisible when hideOptions is called', () => {

    // given
    component.edit = true;
    component.optionsVisible = true;

    // when
    component.hideOptions();

    // then
    expect(component.edit).toBe(false);
    expect(component.optionsVisible).toBe(false);
  });

  it('should set false for edit when cancelEdit is called', () => {

    // given
    component.edit = true;

    // when
    component.cancelEdit();

    // then
    expect(component.edit).toBe(false);
  });

  it('should set true for edit when performEdit is called', () => {

    // given
    component.edit = false;

    // when
    component.performEdit();

    // then
    expect(component.edit).toBe(true);
  });

  it('should set new value for tree node value when the value is different', () => {

    // given
    const oldValue = 0;
    const newValue = 1;
    component.treeNodeValue = oldValue;
    component.ngOnInit();

    // when
    component.newValue.setValue(newValue);
    component.acceptEditWithNewValue();

    // then
    expect(component.treeNodeValue).toBe(newValue);
  });

  it('should emit updateNodeValueEvent when acceptEditWithNewValue is called and tree node value is different', () => {

    // given
    spyOn(component.updateNodeValueEvent, 'emit');
    component.ngOnInit();

    // when
    component.newValue.setValue(1);
    component.acceptEditWithNewValue();

    // then
    expect(component.updateNodeValueEvent.emit).toHaveBeenCalled();
  });

  it('should set false for edit when acceptEditWithNewValue is called', () => {

    // given
    component.edit = true;
    component.ngOnInit();

    // when
    component.acceptEditWithNewValue();

    // then
    expect(component.edit).toBe(false);
  });
});
