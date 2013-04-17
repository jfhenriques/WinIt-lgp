<?php

	class Address extends ActiveRecord {

		private $adid;
		private $cp4;
		private $cp3;

		private $district;
		private $locality;

		private $street;

		prib



		public function getAddressID()
		{
			return $this->adid;
		}
		public function getCP4()
		{
			return $this->cp4;
		}
		public function getCP3()
		{
			return $this->cp3;
		}




	}

?>