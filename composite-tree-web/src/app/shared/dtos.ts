/**
 * Transfer object from the backend
 */
export interface ComponentNode {
  id: number;
  value: number;
  children?: ComponentNode[];
}
