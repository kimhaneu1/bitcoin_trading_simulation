import React from 'react';

const AdvertisementBanner: React.FC = () => {
  return (
    <div className="bg-sky-100 rounded-md shadow border p-4 text-center">
      <img src="/images/advertisement.png" alt="Advertisement" className="mx-auto mb-2" />
    </div>
  );
};

export default AdvertisementBanner;
