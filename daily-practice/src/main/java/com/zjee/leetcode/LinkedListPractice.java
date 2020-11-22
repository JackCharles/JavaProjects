package com.zjee.leetcode;

/**
 * @author zhongjie
 */
public class LinkedListPractice {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;

        //l1向l2中插入
        ListNode q = l2;
        while (l1 != null) {
            ListNode p = l1.next;
            //头节点特殊处理
            if (q == l2 && q.val > l1.val) {
                l1.next = q;
                q = l1;
                l2 = q;
            } else {
                while (q.next != null && q.next.val <= l1.val) {
                    q = q.next;
                }
                l1.next = q.next;
                q.next = l1;
                q = q.next;
            }
            l1 = p;
        }

        return l2;
    }


    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(String.valueOf(val));
            ListNode p = this.next;
            while (p != null) {
                builder.append("->").append(p.val);
                p = p.next;
            }
            return builder.toString();
        }
    }

    public static void main(String[] args) {
        ListNode l1 = createList(new int[]{1, 1, 2});
        ListNode l2 = createList(new int[]{9, 9});

//        System.out.println(new LinkedListPractice().mergeTwoLists(l1, l2));
//        System.out.println(new LinkedListPractice().recursiveReverseList(l1));
//        System.out.println(new LinkedListPractice().addTwoNumbers(l1, l2));
        System.out.println(new LinkedListPractice().sortList(l1));
    }

    public static ListNode createList(int[] arr) {
        ListNode p = null;
        ListNode q = null;
        for (int i : arr) {
            if (p == null) {
                p = new ListNode(i);
                q = p;
            } else {
                q.next = new ListNode(i);
                q = q.next;
            }
        }

        return p;
    }

    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode p = head.next;
        head.next = null;
        while (p != null) {
            ListNode t = p.next;
            p.next = head;
            head = p;
            p = t;
        }

        return head;
    }


    public ListNode recursiveReverseList(ListNode head) {
        ListNode rHead = new ListNode(0);
        recursiveReverseList(head, rHead);
        return rHead.next;
    }


    public ListNode recursiveReverseList(ListNode head, ListNode rHead) {
        if (head == null || head.next == null) {
            rHead.next = head;
            return head;
        }
        ListNode listNode = recursiveReverseList(head.next, rHead);
        head.next = null;
        listNode.next = head;
        return listNode.next;
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        //将l1加到l2上
        int carry = 0;
        ListNode head = l2;
        while (l1 != null && l2.next != null) {
            int t = l1.val + l2.val + carry;
            l2.val = t % 10;
            carry = t / 10;

            l1 = l1.next;
            l2 = l2.next;
        }

        if (l1 == null) {
            while (carry > 0) {
                int t = l2.val + carry;
                l2.val = t % 10;
                carry = t / 10;
                if (l2.next == null && carry > 0) {
                    l2.next = new ListNode(0);
                }
                l2 = l2.next;
            }
        } else {
            while (l1 != null || carry > 0) {
                int t = (l1 == null ? 0 : l1.val) + l2.val + carry;
                l2.val = t % 10;
                carry = t / 10;

                if (l1 != null) {
                    l1 = l1.next;
                }
                if (l1 != null || carry > 0) {
                    l2.next = new ListNode(0);
                    l2 = l2.next;
                }
            }
        }

        return head;
    }


    //归并排序，核心：快慢指针找中点
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode f = head;
        ListNode s = head;
        ListNode t = head;
        while (f != null) {
            t = s;
            s = s.next;
            f = f.next;
            if (f != null) {
                f = f.next;
            }
        }

        t.next = null;
        return mergeTwoLists(sortList(head), sortList(s));
    }


    public ListNode detectCycle(ListNode head) {
        if (head == null) {
            return null;
        }

        ListNode s = head;
        ListNode f = head;
        while (f != null) {
            s = s.next;
            f = f.next;
            if (f != null) {
                f = f.next;
            }
            //相遇
            if (s == f) {
                s = head;
                while (s != f) {
                    s = s.next;
                    f = f.next;
                }
                return s;
            }
        }
        return null;
    }

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }

        ListNode p = headA;
        ListNode q = headB;
        while (p != null && q != null) {
            if (p == q) {
                return p;
            }
            p = p.next;
            q = q.next;
        }

        //B长
        ListNode longer = p == null ? headB : headA;
        ListNode shorter = longer == headA ? headB : headA;
        p = p == null ? q : p;
        int diff = 0;
        while (p != null) {
            diff++;
            p = p.next;
        }

        for (int i = 0; i < diff; i++) {
            longer = longer.next;
        }

        while (longer != null) {
            if (longer == shorter) {
                return longer;
            }
            longer = longer.next;
            shorter = shorter.next;
        }
        return null;
    }

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        } else if (lists.length == 1) {
            return lists[0];
        }
        return curMergeKLists(lists, 0, lists.length - 1);
    }

    public ListNode curMergeKLists(ListNode[] listNodes, int start, int end) {
        if (start == end) {
            return listNodes[start];
        } else if (start + 1 == end) {
            return mergeTwoLists(listNodes[start], listNodes[end]);
        } else {
            int mid = (start + end) / 2;
            return mergeTwoLists(curMergeKLists(listNodes, start, mid), curMergeKLists(listNodes, mid + 1, end));
        }
    }

}
