import { ComponentNode } from '../shared/dtos';
import { ComponentFlatNode } from '../shared/interfaces';

/**
 * Util class for maintain the tree and its nodes in the DOM
 */
export class TreeUtils {
  static hasChild = (_: number, node: ComponentFlatNode) => node.expandable;

  static transformer = (node: ComponentNode, level: number) => {
    return {
      expandable: !!node.children && node.children.length > 0,
      value: '' + node.value,
      level,
      id: node.id
    };
  }
}
