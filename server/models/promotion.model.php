<?php

	class Promotion extends ActiveRecord {
	
		private $pid = null;
		private $name;
		private $init_date;
		private $end_date;
		private $user_limit;
		private $valid_coord;
		private $valid_coord_radius;
		private $transferable;
		private $active;
		private $win_points;
		private $rid;
		private $ptid;
		private $func_type;
		private $grand_limit;
		
		/* GET's and SET's*/

		public function getID()
		{
			return $this->pid;
		}
	
		public function getName()
		{
			return $this->name;
		}
		public function setName($name)
		{
			$this->nome = $name;
		}
		
		public function getInit_date()
		{
			return $this->init_date;
		}
		public function setInit_date($init_date)
		{
			$this->init_date = $init_date;
		}
		
		public function getEnd_date()
		{
			return $this->end_date;
		}
		public function setEnd_date($end_date)
		{
			$this->end_date = $end_date;
		}
		
		public function getUser_limit()
		{
			return $this->user_limit;
		}
		public function setUser_limit($user_limit)
		{
			$this->user_limit = $user_limit;
		}
		
		public function getValid_coord()
		{
			return $this->valid_coord;
		}
		public function setValid_coord($valid_coord)
		{
			$this->valid_coord = $valid_coord;
		}
		
		public function getValid_coord_radius()
		{
			return $this->valid_coord_radius;
		}
		public function setValid_coord_radius($valid_coord_radius)
		{
			$this->valid_coord_radius = $valid_coord_radius;
		}
		
		public function getTransferable()
		{
			return $this->transferable;
		}
		public function setTransferable($transferable)
		{
			$this->transferable = $transferable;
		}
		
		public function getActive()
		{
			return $this->active;
		}
		public function setActive($active)
		{
			$this->active = $active;
		}
		
		public function getWin_points()
		{
			return $this->win_points;
		}
		public function setWin_points($win_points)
		{
			$this->win_points = $win_points;
		}
		
		public function getRid()
		{
			return $this->rid;
		}
		public function setRid($rid)
		{
			$this->rid = $rid;
		}
		
		public function getPtid()
		{
			return $this->ptid;
		}
		public function setPtid$ptid)
		{
			$this->ptid = $ptid;
		}
		
		
		public function getFunc_type()
		{
			return $this->func_type;
		}
		public function setFunc_type($func_type)
		{
			$this->func_type = $func_type;
		}
		
		public function getGrand_limit()
		{
			return $this->grand_limit;
		}
		public function setGrand_limit($grand_limit)
		{
			$this->grand_limit = $grand_limit;
		}
	}

?>