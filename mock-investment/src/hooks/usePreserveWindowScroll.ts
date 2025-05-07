// src/hooks/usePreserveWindowScroll.ts
import { useLayoutEffect, useRef } from 'react';

/**
 * 페이지 전체(window)의 스크롤 위치를 상태 업데이트 전후에 보존합니다.
 * 소켓 등으로 리렌더링이 발생해도 스크롤이 유지되게 합니다.
 */
export function usePreserveWindowScroll(deps: any[] = []) {
  const scrollYRef = useRef(0);

  useLayoutEffect(() => {
    scrollYRef.current = window.scrollY;

    return () => {
      requestAnimationFrame(() => {
        window.scrollTo({ top: scrollYRef.current, behavior: 'auto' });
      });
    };
  }, deps);
}
