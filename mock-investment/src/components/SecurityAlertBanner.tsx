import React, { useState } from 'react';
import { AlertTriangle, X } from 'lucide-react';

interface SecurityAlertBannerProps {
  className?: string;
  persistent?: boolean;
}

const SecurityAlertBanner: React.FC<SecurityAlertBannerProps> = ({ 
  className = '',
  persistent = false 
}) => {
  const [isVisible, setIsVisible] = useState(true);
  
  if (!isVisible) return null;
  
  return (
    <div id="Ads" className={`bg-sky-100 border-l-4 border-sky-500 p-4 ${className}`}>
      <div className="flex items-start">
        <div className="flex-shrink-0">
          <AlertTriangle className="h-5 w-5 text-sky-500" />
        </div>
        
        <div className="ml-3 flex-1">
          <div className="flex justify-end items-start">
            
            {!persistent && (
              <button 
                className="ml-4 flex-shrink-0 text-sky-500 hover:text-sky-700 focus:outline-none"
                onClick={() => setIsVisible(false)}
              >
                <X className="h-4 w-4" />
              </button>
            )}
          </div>
          
          <div className="mt-2 text-sm text-sky-700">
              <img src="/images/security-alert.png" alt="Security Alert"/>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SecurityAlertBanner;